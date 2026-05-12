package com.judgemesh.dispatcher.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class LeaderElectionService {
    private final AtomicBoolean leader = new AtomicBoolean(true);
    private final String podName;
    private volatile Instant lastChangedAt = Instant.now();

    public LeaderElectionService(@Value("${HOSTNAME:dispatcher-local}") String podName) {
        this.podName = podName;
    }

    public boolean isLeader() {
        return leader.get();
    }

    public Map<String, Object> status() {
        return Map.of(
                "leader", leader.get() ? podName : null,
                "self", podName,
                "isLeader", leader.get(),
                "mode", "local-single-leader",
                "lastChangedAt", lastChangedAt.toString());
    }

    public void relinquishForChaos() {
        leader.set(false);
        lastChangedAt = Instant.now();
    }

    public void becomeLeader() {
        leader.set(true);
        lastChangedAt = Instant.now();
    }
}
