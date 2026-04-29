package config

import (
	"os"
)

type Config struct {
	ListenAddr string
	WorkerID   string

	// Sprint 1 由 A 实装
	RabbitMQURL    string
	SubmitQueue    string
	CallbackBase   string
	IsolateBoxRoot string
}

func Load() Config {
	return Config{
		ListenAddr:     getenv("LISTEN_ADDR", ":8090"),
		WorkerID:       getenv("WORKER_ID", "worker-local"),
		RabbitMQURL:    getenv("RABBITMQ_URL", "amqp://guest:guest@127.0.0.1:5672/"),
		SubmitQueue:    getenv("SUBMIT_QUEUE", "submit.queue"),
		CallbackBase:   getenv("CALLBACK_BASE", "http://submit-service:8083"),
		IsolateBoxRoot: getenv("ISOLATE_BOX_ROOT", "/var/local/lib/isolate"),
	}
}

func getenv(k, def string) string {
	if v := os.Getenv(k); v != "" {
		return v
	}
	return def
}
