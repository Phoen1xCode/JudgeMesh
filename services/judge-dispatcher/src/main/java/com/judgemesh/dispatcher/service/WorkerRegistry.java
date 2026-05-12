package com.judgemesh.dispatcher.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WorkerRegistry {
    private final List<URI> workers;
    private final Map<URI, Integer> inflight = new ConcurrentHashMap<>();

    public WorkerRegistry(@Value("${judgemesh.worker.urls:http://127.0.0.1:8090}") String workerUrls) {
        this.workers = Arrays.stream(workerUrls.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(URI::create)
                .toList();
    }

    public URI acquire() {
        if (workers.isEmpty()) {
            throw new IllegalStateException("no worker urls configured");
        }
        URI worker = workers.stream()
                .min(Comparator.comparing(uri -> inflight.getOrDefault(uri, 0)))
                .orElseThrow();
        inflight.merge(worker, 1, Integer::sum);
        return worker;
    }

    public void release(URI worker) {
        inflight.computeIfPresent(worker, (ignored, count) -> Math.max(0, count - 1));
    }

    public Map<String, Integer> inflightSnapshot() {
        return inflight.entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(e -> e.getKey().toString(), Map.Entry::getValue));
    }
}
