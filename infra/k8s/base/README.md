# infra/k8s/base/ — 基础 manifest

每个目录代表一个部署单元。`submit-service/` 与 `judge-worker/` 是**参考样板**,已可直接 apply;其余服务复制 `submit-service/deployment.yaml` 改名 + 端口即可。

## 业务服务对应端口

| 服务              | 端口 | 命名空间   |
| ----------------- | ---- | ---------- |
| gateway           | 8080 | judgemesh  |
| user-service      | 8081 | judgemesh  |
| problem-service   | 8082 | judgemesh  |
| submit-service    | 8083 | judgemesh  |
| judge-dispatcher  | 8084 | judgemesh  |
| judge-worker      | 8090 | judgemesh  |

## 中间件命名空间

| 组件                                              | 部署方式                           | 命名空间          |
| ------------------------------------------------- | ---------------------------------- | ----------------- |
| nacos                                             | nacos-k8s helm chart               | judgemesh-infra   |
| mysql                                             | StatefulSet + PVC                  | judgemesh-infra   |
| redis                                             | StatefulSet + PVC                  | judgemesh-infra   |
| rabbitmq                                          | RabbitMQ Cluster Operator          | judgemesh-infra   |
| etcd                                              | StatefulSet 3 副本                 | judgemesh-infra   |
| minio                                             | MinIO Operator                     | judgemesh-infra   |
| seata-server                                      | StatefulSet                        | judgemesh-infra   |
| ingress-nginx                                     | helm                               | ingress-nginx     |
| cert-manager                                      | helm                               | cert-manager      |
| prometheus / grafana / alertmanager / skywalking / loki | helm(详见 ../helm/values/)  | judgemesh-observe |
| chaos-mesh                                        | helm                               | chaos-mesh        |

> Sprint 0 目标:把每个目录的 `deployment.yaml` / `statefulset.yaml` 占位先 push,Sprint 1 再细化资源 / 探针 / PVC。
