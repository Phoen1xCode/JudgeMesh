package server

import (
	"encoding/json"
	"log/slog"
	"net/http"

	"github.com/judgemesh/judge-worker/internal/config"
	"github.com/judgemesh/judge-worker/internal/judge"
)

type Server struct {
	cfg     config.Config
	version string
	runner  *judge.Runner
}

func New(cfg config.Config, version string) *Server {
	return &Server{
		cfg:     cfg,
		version: version,
		runner:  judge.NewRunner(cfg),
	}
}

// Handler 用 Go 1.22 ServeMux 模式路由。
func (s *Server) Handler() http.Handler {
	mux := http.NewServeMux()
	mux.HandleFunc("GET /health", s.health)
	mux.HandleFunc("GET /metrics", s.metrics) // TODO Sprint 1 接 prometheus client
	mux.HandleFunc("POST /judge", s.judge)
	return mux
}

func (s *Server) health(w http.ResponseWriter, r *http.Request) {
	writeJSON(w, http.StatusOK, map[string]any{
		"status":   "UP",
		"workerId": s.cfg.WorkerID,
		"version":  s.version,
	})
}

func (s *Server) metrics(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "text/plain; version=0.0.4")
	// TODO Sprint 1: 接入 prometheus/client_golang
	_, _ = w.Write([]byte("# HELP judge_worker_up 1 if up\n# TYPE judge_worker_up gauge\njudge_worker_up 1\n"))
}

func (s *Server) judge(w http.ResponseWriter, r *http.Request) {
	var task judge.Task
	if err := json.NewDecoder(r.Body).Decode(&task); err != nil {
		writeJSON(w, http.StatusBadRequest, map[string]string{"error": "bad json: " + err.Error()})
		return
	}
	slog.Info("judge accepted", "submitId", task.SubmitID, "lang", task.Language)

	// 占位:同步返回 ACCEPTED;真正实现走异步 channel + worker pool + isolate
	go s.runner.Run(r.Context(), task) // nolint:contextcheck

	writeJSON(w, http.StatusAccepted, map[string]any{
		"accepted":  true,
		"submitId":  task.SubmitID,
		"workerId":  s.cfg.WorkerID,
		"workerVer": s.version,
	})
}

func writeJSON(w http.ResponseWriter, status int, v any) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	_ = json.NewEncoder(w).Encode(v)
}
