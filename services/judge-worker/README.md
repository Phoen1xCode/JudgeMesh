# judge-worker

> A · Go 1.22 + isolate · docs/design/05-判题流水线.md / docs/design/12-五人分工.md §A

## 本地运行

```bash
go run ./cmd/worker
# 另开终端
curl localhost:8090/health
curl -X POST localhost:8090/judge -H 'Content-Type: application/json' -d '{
  "submit_id": 1, "problem_id": 1, "language": "cpp",
  "source": "int main(){}", "time_limit_ms": 1000, "memory_limit_mb": 256,
  "testcases": [{"name":"01","input_url":"","expected_output_url":""}],
  "callback_url": "http://localhost:8083/api/submits/internal/result"
}'
```

## 目录

```
cmd/worker/main.go         入口 + 优雅停机
internal/server/           HTTP 路由(/health /metrics /judge)
internal/judge/            真正的判题逻辑(Sprint 1 实装 isolate + 4 语言)
internal/config/           env 配置
```

## TODO(Sprint 1)

- [ ] isolate 集成
- [ ] 4 语言 runner(C / C++ / Java / Python)
- [ ] MinIO 用例下载
- [ ] RabbitMQ 消费者(替代当前 HTTP /judge 路径)
- [ ] Prometheus 客户端 + 指标
- [ ] 沙箱安全测试(fork bomb、写文件、网络访问)
