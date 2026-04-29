<!-- 标题格式:<type>(<module>): <desc>  示例:feat(worker): isolate 集成 C++ 运行器 -->

## 变更摘要

<!-- 1-3 行,说清"做了什么"+"为什么" -->

## 关联 issue

Closes #

## 自测清单

- [ ] 本地编译通过(`mvn verify` / `go test ./...` / `npm run build`)
- [ ] 单元测试覆盖核心改动
- [ ] 跑过 `/actuator/health`(Java 服务)
- [ ] 不引入新警告(checkstyle / golangci-lint / eslint)

## 影响接口?

<!-- 如果改了 services/api/ 下的 DTO / Feign / MQ schema,@ 所有依赖方 -->
- [ ] 不影响共享 API
- [ ] 影响共享 API,已 @ 相关同学并合并到 main

## 部署影响

<!-- 是否新增环境变量、ConfigMap、Secret、DB 迁移?有就在这里写明白 -->
