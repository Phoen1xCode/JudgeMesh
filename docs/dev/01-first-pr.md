# feat(infra): bootstrap monorepo skeleton with 6 Java modules + Go worker

> commit: `6b517d5` — 87 files changed, 3784 insertions(+), 15 deletions(-)

## Summary

- **统一技术栈基线**:Maven 父 POM 锁定 Spring Boot 3.5.0 / Spring Cloud 2025.0.0 / Spring Cloud Alibaba 2025.0.0.0 / Java 17,所有 Java 模块自动继承,避免后续版本拉锯。
- **铺平 6 个微服务骨架**:`gateway` + `user-service` + `problem-service` + `submit-service` + `judge-dispatcher` + 共享 `services/api`(DTO / Feign client / MQ schema 集中管理),保证 5 人并行开发时不会因为目录约定吵架。
- **Go judge-worker 单独成岛**:Go 1.22、stdlib only、`cmd/server/judge/config` 分层,与 Java 侧通过 `JudgeTask` / `JudgeResult` MQ 消息解耦,后续接 isolate 沙箱时不会污染 JVM 进程。
- **K8s + Helm + Chaos 一次到位**:`infra/k8s/{base,overlays}` 用 kustomize 双层结构,`submit-service` 与 `judge-worker` 给出 HPA 完整样板;`infra/helm/values` 为 Prometheus / Loki / SkyWalking / Chaos-Mesh 准备占位;`infra/chaos` 三份故障注入 YAML 直接可跑。
- **CI 路径过滤**:`ci-java` / `ci-go` / `ci-frontend` 三工作流各自只在相关路径变更时触发,避免改前端等十几分钟 Maven。
- **首次定义共享消息契约**:`services/api/.../message/JudgeTask.java`、`JudgeResult.java` 是 @Phoen1xCode(judge-worker)与 @Nier291(submit-service / judge-dispatcher)之间的合同,需要 Day 2 之前双方对齐字段。

## 变更摘要

### Java(58 文件)

- **父 POM**:`pom.xml`(根)
- **共享模块** `services/api`:
  - `pom.xml`
  - `dto/`:`UserDTO.java`、`ProblemDTO.java`、`SubmitDTO.java`
  - `client/`:`UserClient.java`、`ProblemClient.java`(OpenFeign 接口)
  - `error/`:`ApiResponse.java`、`ErrorCode.java`
  - `message/`:`JudgeTask.java`、`JudgeResult.java`(MQ schema,**@Phoen1xCode↔@Nier291 共享合同**)
- **gateway**:`pom.xml`、`GatewayApplication.java`、`application.yml`(SC2025 新坐标 `spring-cloud-starter-gateway-server-webflux`)
- **user-service**:`pom.xml`、`UserApplication.java`、`application.yml`、`db/migration/V1__init.sql`(users_db)
- **problem-service**:同上结构,Flyway 指向 problems_db
- **submit-service**:同上结构,Flyway 指向 submits_db
- **judge-dispatcher**:`pom.xml`、`DispatcherApplication.java`、`application.yml`(无独立库,仅消费 MQ)

### Go(7 文件)

- `services/judge-worker/`:
  - `go.mod`(Go 1.22,stdlib only)
  - `cmd/worker/main.go`(入口)
  - `internal/config/config.go`、`internal/judge/runner.go`、`internal/judge/types.go`、`internal/server/server.go`
  - `Dockerfile`、`README.md`、`.gitignore`(已修正 `worker` 匹配过宽 → `/worker`)

### 基础设施(15 文件)

- `infra/k8s/base/submit-service/{deployment,kustomization}.yaml`、`infra/k8s/base/judge-worker/{deployment,kustomization}.yaml`(含 HPA)
- `infra/k8s/overlays/{dev,prod}/kustomization.yaml`
- `infra/k8s/base/README.md`
- `infra/helm/values/{prometheus,loki,skywalking,chaos-mesh}.yaml`
- `infra/chaos/{kill-leader,kill-worker,mq-down}.yaml`
- `infra/local/{docker-compose.yml,.env.example,README.md}`、`infra/local/mysql-init/01-create-databases.sql`
- `infra/grafana/dashboards/README.md`、`infra/README.md`
- `migrations/README.md`(汇总三库 Flyway 路径)

### CI / 仓库治理(7 文件)

- `.github/workflows/{ci-java,ci-go,ci-frontend}.yml`(全部带 `paths:` 过滤)
- `.github/CODEOWNERS`(占位,需替换为真实 GitHub handles)
- `.github/PULL_REQUEST_TEMPLATE.md`、`.github/ISSUE_TEMPLATE/{bug_report,feature_request}.md`
- `.github/dependabot.yml`

### 根级 / 文档(占位)(5 文件)

- `.gitignore`(扩展)、`.editorconfig`、`CONTRIBUTING.md`、`README.md`(更新)
- `scripts/README.md`、`tests/security/README.md`、`data/demo-problems/README.md`

## 验证

本地已跑过的命令(全绿,**未在本次 commit 时重跑**):

```bash
# Java 侧 —— 7 模块全部成功
mvn -T 4C clean verify -DskipTests
# 输出关键行:
# [INFO] Reactor Summary for judgemesh ...
# [INFO] judgemesh ......................... SUCCESS
# [INFO] services/api ...................... SUCCESS
# [INFO] gateway ........................... SUCCESS
# [INFO] user-service ...................... SUCCESS
# [INFO] problem-service ................... SUCCESS
# [INFO] submit-service .................... SUCCESS
# [INFO] judge-dispatcher .................. SUCCESS
# [INFO] BUILD SUCCESS

# Go 侧
cd services/judge-worker
go vet ./...      # OK
go build ./...    # OK
go test ./...     # OK (no test files yet, ok)
```

CI 工作流首次跑会在推到远端后自动验证(目前仓库无 git remote)。

## 已知 TODO(Sprint 1)

1. **isolate 沙箱集成**:`internal/judge/runner.go` 当前是 stub,需要把 `isolate --init` / `--run` / `--meta` 全流程串起来,并在 K8s privileged 容器内验证。
2. **4 语言运行器**:C / C++ / Java / Python 的编译&运行命令、内存/时间限制映射,需要补到 `judge/runner.go` 的 language profile。
3. **@Phoen1xCode↔@Nier291 共享 schema 复核**:`services/api/.../message/JudgeTask.java` + `JudgeResult.java` 字段(尤其 `verdict` 枚举、`metrics` 子结构)必须 Day 2 之前由 @Phoen1xCode(worker 负责人)和 @Nier291(submit / dispatcher 负责人)联合 review。
4. **Seata 真正接入**:目前 `submit-service`、`problem-service` 只锁了 Spring Cloud Alibaba 版本,AT 模式 / TCC 注解尚未落地;依赖 Nacos 注册和 `undo_log` 表,需要在 Sprint 1 结束前打通。
5. **CODEOWNERS 替换占位**:`.github/CODEOWNERS` 内现是占位用户名,需 5 位组员加入仓库后替换。
6. **Nacos 配置中心切换**:目前各服务 `application.yml` 是本地占位,需要把数据源 / Redis / RabbitMQ 配置抽到 Nacos `dataId`。
7. **OpenAPI 契约导出**:`services/api` 已经定义 Feign 客户端,但缺 springdoc 配置,需要在网关或单服务上挂 `/v3/api-docs` 用于 mock & 联调。
8. **frontend 接 gateway**:目前 `frontend/` 是 Vite 默认模板,需要补 axios/SWR + 路由 + 登录态对接。

## 影响接口?

- [x] **影响共享 API**。`services/api/src/main/java/com/judgemesh/api/message/JudgeTask.java` 与 `JudgeResult.java` 是首次定义,作为 @Phoen1xCode(judge-worker)与 @Nier291(submit-service / judge-dispatcher)之间 RabbitMQ 通信的合同,**必须** Day 2 之前由两位 owner 联合复核字段命名、必填项、枚举值,再有人开始写 producer/consumer。
- [x] **影响共享 Feign client**。`services/api/src/main/java/com/judgemesh/api/client/{UserClient,ProblemClient}.java` 是其他服务调用 user / problem 的入口,后续 user-service / problem-service 的 Controller 路径必须与之保持一致。

## 部署影响

### 新增 / 必需的环境变量(已在 `infra/local/.env.example` 列出)

| 变量名 | 默认值(本地) | 说明 |
| --- | --- | --- |
| `NACOS_ADDR` | `nacos:8848` | Nacos 服务发现 + 配置中心 |
| `MYSQL_HOST` | `mysql` | MySQL 主机(三库 schema 隔离) |
| `MYSQL_PORT` | `3306` | |
| `MYSQL_ROOT_PASSWORD` | (从 secret 注入) | 仅 docker-compose 启动时使用 |
| `REDIS_HOST` | `redis` | Redis(限流 / 会话 / 缓存) |
| `REDIS_PORT` | `6379` | |
| `RABBITMQ_HOST` | `rabbitmq` | 判题任务队列 |
| `RABBITMQ_PORT` | `5672` | |
| `MINIO_ENDPOINT` | `http://minio:9000` | 题面 / 用例 / 提交代码对象存储 |
| `MINIO_ACCESS_KEY` | (从 secret 注入) | |
| `MINIO_SECRET_KEY` | (从 secret 注入) | |
| `JUDGE_WORKER_GRPC_PORT` | `50051` | judge-worker 监听端口 |

### 需要在 K8s 集群里准备的 Secret

- `judgemesh-mysql`:`username`、`password`(供三个 Java 服务挂载到 `SPRING_DATASOURCE_*`)
- `judgemesh-rabbitmq`:`username`、`password`
- `judgemesh-minio`:`access-key`、`secret-key`
- `judgemesh-nacos`:如启用鉴权则需 `username` / `password`

### kustomize overlay

- `infra/k8s/overlays/dev` 与 `prod` 当前仅 `kustomization.yaml` 占位,Sprint 1 部署时需补 `namespace`、`replicas` patch、镜像 tag patch。

---

🤖 Generated with [Claude Code](https://claude.com/claude-code)
