# tests/security/ — 沙箱安全测试用例(A 维护)

每个文件都是**应当被 isolate 沙箱拦截**的恶意代码。Sprint 1 末必须全部用例都被正确分类(RE / TLE / MLE 等),不能 AC、不能逃逸。

## 用例清单(TODO Sprint 1)

| 文件                 | 攻击面                                | 期望结果              |
| -------------------- | ------------------------------------- | --------------------- |
| `fork-bomb.c`        | 无限 fork 耗尽进程数                  | RE / TLE,Pod 不挂    |
| `write-file.c`       | 写宿主文件系统                        | RE(权限拒绝)        |
| `read-etc-passwd.c`  | 读 /etc/passwd                        | RE / 沙箱外不可见     |
| `network-egress.py`  | 主动外联 1.1.1.1:80                   | RE(网络隔离)        |
| `infinite-loop.cpp`  | 死循环                                | TLE                   |
| `mem-balloon.cpp`    | 申请超量内存                          | MLE                   |
| `large-output.py`    | 输出 100MB                            | OLE(Output Limit)    |

## 跑法

```bash
# Sprint 1 由 A 写驱动
go test ./services/judge-worker/internal/judge -run TestSandbox -v
```
