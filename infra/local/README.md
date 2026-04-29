# 本地中间件一键栈

```bash
# 首次
cp infra/local/.env.example infra/local/.env
docker compose -f infra/local/docker-compose.yml --env-file infra/local/.env up -d

# 看状态
docker compose -f infra/local/docker-compose.yml ps

# 销毁(连数据)
docker compose -f infra/local/docker-compose.yml down -v
```

| 服务      | 端口          | 控制台 / 凭据                                                       |
| --------- | ------------- | ------------------------------------------------------------------- |
| MySQL     | 3306          | `judgemesh / judgemesh`,3 库已建好                                   |
| Redis     | 6379          | 无密码                                                               |
| RabbitMQ  | 5672 / 15672  | http://localhost:15672 · `judgemesh / judgemesh`                    |
| MinIO     | 9000 / 9001   | http://localhost:9001 · `minioadmin / minioadmin`,3 桶已建好        |
| etcd      | 2379          | `etcdctl --endpoints=http://localhost:2379 endpoint health`         |
| Nacos     | 8848 / 9848   | http://localhost:8848/nacos · 默认无鉴权(`NACOS_AUTH_ENABLE=false`) |

详见 [docs/dev/01-本地环境配置.md](../../docs/dev/01-本地环境配置.md)。
