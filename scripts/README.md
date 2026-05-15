# scripts

| Script | Purpose | Owner | Status |
| ------ | ------- | ----- | ------ |
| `k8s-bootstrap.sh` | kubeadm cluster bootstrap | @sigufh | TODO |
| `import-problems.py` | bulk import problems and testcases to MinIO | @KY-raika | TODO |
| `loadtest-submit.lua` | wrk load test for submit-service submit path | @sigufh + @Nier291 | DONE |
| `gen-jwt.sh` | generate test JWT for debugging | @Phoen1xCode | TODO |
| `port-forward.sh` | port-forward Nacos / Grafana / SkyWalking locally | @sigufh | TODO |

Run submit load test:

```bash
wrk -t4 -c50 -d5m -s scripts/loadtest-submit.lua http://127.0.0.1:8083
```

Optional variables:

```bash
USER_ID=1001 PROBLEM_ID=1 LANGUAGE=cpp wrk -t4 -c50 -d5m -s scripts/loadtest-submit.lua http://127.0.0.1:8083
```
