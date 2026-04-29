# ─── Stage 1: build Go binary ─────────────────────────────────────
FROM golang:1.22-bookworm AS builder
WORKDIR /src
COPY go.mod ./
RUN go mod download
COPY . .
RUN CGO_ENABLED=0 GOOS=linux go build -o /out/worker ./cmd/worker

# ─── Stage 2: runtime with isolate + 4 toolchains ─────────────────
# Sprint 1 由 A 完整化:isolate / gcc / g++ / openjdk-17 / python3
FROM ubuntu:22.04
ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get install -y --no-install-recommends \
        ca-certificates curl \
        # 编译期工具(用于编译用户代码)
        gcc g++ make \
        openjdk-17-jdk-headless \
        python3 \
        # isolate 依赖
        libcap-dev pkg-config \
    && rm -rf /var/lib/apt/lists/*

# TODO Sprint 1: 从 https://github.com/ioi/isolate clone + make + install
# RUN git clone https://github.com/ioi/isolate /tmp/isolate \
#  && cd /tmp/isolate && make && make install \
#  && rm -rf /tmp/isolate

COPY --from=builder /out/worker /usr/local/bin/worker

EXPOSE 8090
USER root
ENTRYPOINT ["/usr/local/bin/worker"]
