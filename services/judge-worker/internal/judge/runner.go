package judge

import (
	"context"
	"log/slog"

	"github.com/judgemesh/judge-worker/internal/config"
)

// Runner 真正的判题协调器。Sprint 1 由 A 接入 isolate 沙箱与 4 语言运行器。
type Runner struct {
	cfg config.Config
}

func NewRunner(cfg config.Config) *Runner {
	return &Runner{cfg: cfg}
}

// Run 占位实现。真实流程:
//  1. 下载用例文件(MinIO 预签名 URL)
//  2. 编译(C / C++ / Java / Python)
//  3. 用 isolate 跑每条用例(time/memory limit)
//  4. 对比预期输出
//  5. POST 结果到 callback_url
func (r *Runner) Run(ctx context.Context, t Task) {
	slog.Info("TODO: actually judge",
		"submitId", t.SubmitID,
		"problemId", t.ProblemID,
		"language", t.Language,
		"cases", len(t.Testcases))

	// TODO Sprint 1
	_ = r.cfg
	_ = ctx
}
