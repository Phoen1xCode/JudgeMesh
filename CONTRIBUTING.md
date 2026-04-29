# 贡献指南

> 全队 5 人协作的最小公约数。完整背景见 [docs/design/12-五人分工.md](docs/design/12-五人分工.md)。

## 必读文档

| 主题                    | 链接                                                             |
| ----------------------- | ---------------------------------------------------------------- |
| 设计文档总入口          | [docs/design/README.md](docs/design/README.md)                   |
| **Sprint 0 初始化指南** | [docs/dev/00-初始化指南.md](docs/dev/00-初始化指南.md)           |
| Spring Boot 3.5         | https://docs.spring.io/spring-boot/docs/3.5.x/reference/html/    |
| Spring Cloud 2025       | https://docs.spring.io/spring-cloud/docs/current/reference/html/ |
| Spring Cloud Alibaba    | https://sca.aliyun.com/docs/                                     |
| Nacos                   | https://nacos.io/docs/                                           |
| Sentinel                | https://sentinelguard.io/zh-cn/docs/                             |
| Seata                   | https://seata.apache.org/zh-cn/docs/                             |
| Kubernetes              | https://kubernetes.io/docs/home/                                 |
| Helm                    | https://helm.sh/docs/                                            |
| isolate 沙箱            | https://github.com/ioi/isolate                                   |
| Go 1.22                 | https://go.dev/doc/                                              |

## 版本(已锁,禁止子模块覆盖)

- JDK 17 / Spring Boot 3.5.0 / Spring Cloud 2025.0.0 / Spring Cloud Alibaba 2025.0.0.0
- Go 1.22 / Node 20 LTS / Kubernetes 1.28

## Git 协作

### 分支命名

```
feat/${person}-${module}-${desc}     # 新功能
fix/${person}-${module}-${desc}      # 缺陷修复
docs/${person}-${desc}               # 仅文档
```

示例:`feat/a-worker-isolate-cpp`、`fix/d-submit-mq-retry`。

### Commit message

```
<type>(<module>): <desc>
```

`type` ∈ `{feat, fix, docs, refactor, test, chore}`。
示例:`feat(worker): isolate 集成 C++ 运行器`。

### PR 流程

1. 从最新 `main` 切分支,小步提交
2. 推送 → 在 GitHub 提 PR,模板自动加载
3. CI 必须绿;CODEOWNERS 自动指派至少 1 人 review
4. **squash merge**(线性历史)
5. 合并后删除分支

## 接口变更协议

`services/api/` 任何改动需要:

1. PR 标题前缀加 `[api-change]`
2. 在 PR 描述里 @ 所有依赖方(看 [docs/design/12-五人分工.md](docs/design/12-五人分工.md) 协作图)
3. **A 守门审批**(组长最终决策)
4. 各依赖方在 24h 内 pull 最新 API 并适配

特别地,Worker ↔ Dispatcher 协议(`JudgeTask` / `JudgeResult`)由 A 与 D 共同维护,改动 PR 双签确认。

## 紧急升级路径

| 严重度      | 反应      | 渠道           |
| ----------- | --------- | -------------- |
| 🔴 Critical | 立即响应  | 群语音 + @全员 |
| 🟡 High     | 1h 内响应 | 群消息         |
| 🟢 Normal   | 当天响应  | 群消息 / Issue |

## 沟通节奏

- **每周一次 1h 长会**:进度、对齐接口、分配下周
- **每天 15min 文字站会**:昨天 / 今天 / blocker
- 重要决策 → commit message
