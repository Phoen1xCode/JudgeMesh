# 分布式在线判题系统(Distributed OJ)

> 武汉大学《分布式软件原理与技术》课程大作业 · 2026 春
> 团队规模:5 人 · 交付周期:4 周(第 9~12 周)
> 项目代号:**JudgeMesh**

## 项目一句话

一个采用 Spring Cloud Alibaba 微服务底盘、运行在 Kubernetes 上、通过 isolate 沙箱执行多语言提交的分布式在线判题平台,集成可观测性大盘与混沌实验,作为分布式三高(高并发、高性能、高可用)的端到端范例。

## 文档导航

| #   | 文档                                 | 用途                                     | 答辩对应             |
| --- | ------------------------------------ | ---------------------------------------- | -------------------- |
| 01  | [需求文档](./01-需求文档.md)         | 用户角色、功能性 / 非功能性需求          | **第 10 周需求文档** |
| 02  | [总体架构](./02-总体架构.md)         | 架构总图、技术选型、CAP/PACELC 取舍      | **第 10 周概要设计** |
| 03  | [服务拆分与接口](./03-服务拆分.md)   | 微服务职责边界、对外 API                 | 第 10 周概要设计     |
| 04  | [数据模型与存储](./04-数据模型.md)   | MySQL Schema / Redis / MQ / MinIO        | 第 10 周概要设计     |
| 05  | [判题流水线](./05-判题流水线.md)     | 核心子系统:提交→派发→沙箱→回写           | 第 10 周概要设计     |
| 06  | [服务治理与容错](./06-服务治理.md)   | Nacos / Sentinel / Seata / 混沌          | 第 10 周概要设计     |
| 07  | [可观测性方案](./07-可观测性.md)     | Prometheus / Grafana / SkyWalking / Loki | 第 10 周概要设计     |
| 08  | [部署架构](./08-部署架构.md)         | K8s 集群、Ingress、CI/CD、HPA            | 第 10 周概要设计     |
| 09  | [课程主题映射](./09-课程主题映射.md) | 8 讲 PPT 知识点 → 项目落地点             | **答辩素材**         |
| 10  | [测试策略](./10-测试策略.md)         | 单元/集成/压测/混沌                      | **第 12 周运行测试** |
| 11  | [四周排期](./11-四周排期.md)         | Week 1-4 里程碑 + 检查点对齐             | 项目管理             |
| 12  | [五人分工](./12-五人分工.md)         | @Phoen1xCode 组长 + @HashThiin-@sigufh 后端均分                    | 项目管理             |

## 阅读顺序建议

- **老师 / 答辩组**:01 → 02 → 09(看需求 → 看架构 → 看课程契合度)
- **新加入的组员**:01 → 02 → 12(自己的模块) → 03/04 与该模块相关章节
- **代码评审**:03 → 04 → 05 → 06,然后看仓库

## 关键决策摘要(Why we chose what)

| 决策点     | 选择                               | 替代方案                           | 理由                                                                      |
| ---------- | ---------------------------------- | ---------------------------------- | ------------------------------------------------------------------------- |
| 微服务底盘 | Spring Cloud Alibaba               | Netflix OSS / K8s 原生             | Netflix 已停维护;Alibaba 注册+配置二合一 + Seata 直接命中"分布式事务"一讲 |
| 容器编排   | Kubernetes(全部上)                 | Docker Compose                     | 命中"容器编排 / 云原生"主题,HPA 弹性伸缩演示                              |
| 判题沙箱   | Worker Pod + isolate               | K8s Job per submission / 纯 Docker | judge0/QDUOJ 同源做法,启动快、隔离严、社区文档全                          |
| 调度选主   | etcd Lease                         | 自实现 Raft / 单点                 | 30 行代码命中"分布式共识",不重造轮子                                      |
| 消息队列   | RabbitMQ                           | Kafka / Redis Streams              | 学习曲线低、管理面好看,本场景不需要 Kafka 的吞吐                          |
| 排行榜     | Redis ZSet                         | DB 排序 + 缓存                     | O(log n) 写入 + O(1) 读取,直接命中"高并发设计-缓存"一讲                   |
| 实时推送   | WebSocket                          | SSE / 轮询                         | 比赛场景双向交互更自然                                                    |
| 可观测性   | Prom + Grafana + SkyWalking + Loki | ELK / Datadog                      | 全开源、K8s 友好、自带链路追踪                                            |

## 团队 5 人分工(摘要)

> **@Phoen1xCode 是团队 Go 主力**,所以 judge-worker(Go)归 @Phoen1xCode;@Phoen1xCode 也写 Java(网关、共享 API)。
> @Phoen1xCode 完整承担前端(28h);后端 5 人尽量均分(每人 ~50h,std ~6h)。

| 角色         | 主要职责                                                                                | 后端工时 | 总工时          |
| ------------ | --------------------------------------------------------------------------------------- | -------- | --------------- |
| **@Phoen1xCode · 组长** | 前端全套 + **judge-worker(Go + isolate + 4 语言)** + API 网关(Java) + 共享 API 模块     | ~41h     | ~69h(含前端 28) |
| **@HashThiin**        | user-service + Seata 客户端 + Seata Server 部署 + DB 迁移协调 + MySQL 部署              | ~51h     | ~51h            |
| **@KY-raika**        | problem-service + MinIO 部署 + 缓存策略 + 题目导入工具 + 50 道 demo 题                  | ~46h     | ~46h            |
| **@Nier291**        | submit-service + 比赛模块 + Redis ZSet 排行 + WebSocket + judge-dispatcher(etcd 选主)   | ~57h     | ~57h            |
| **@sigufh**        | CI/CD + K8s 集群 + Nacos + 中间件部署 + 可观测性栈 + 4 个 Grafana 大盘 + ChaosMesh 混沌 | ~54h     | ~54h            |

详见 [12-五人分工](./12-五人分工.md)。

## 仓库结构(规划)

```
judgemesh/
├── frontend/              # React + Vite(@Phoen1xCode 负责)
├── services/
│   ├── api/               # 共享 DTO / Feign / MQ schema(@Phoen1xCode 维护)
│   ├── gateway/           # Spring Cloud Gateway(@Phoen1xCode · Java)
│   ├── user-service/      # @HashThiin
│   ├── problem-service/   # @KY-raika
│   ├── submit-service/    # @Nier291(含比赛+WebSocket)
│   ├── judge-dispatcher/  # @Nier291(etcd 选主)
│   └── judge-worker/      # @Phoen1xCode(Go + isolate)
├── infra/                 # 全部 @sigufh 负责
│   ├── k8s/               # K8s manifests
│   ├── helm/              # Helm values
│   ├── grafana/           # 大盘 JSON
│   └── chaos/             # ChaosMesh 实验
├── docs/                  # 你正在看的目录
└── scripts/               # CI/CD、压测脚本
```
