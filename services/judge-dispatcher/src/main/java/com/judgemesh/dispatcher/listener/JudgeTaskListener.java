package com.judgemesh.dispatcher.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.judgemesh.api.message.JudgeTask;
import com.judgemesh.dispatcher.service.DispatchService;
import com.judgemesh.dispatcher.service.DispatcherLeaderService;
import com.judgemesh.dispatcher.service.JudgeTaskRetryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JudgeTaskListener {

    private final ObjectMapper objectMapper;
    private final DispatchService dispatchService;
    private final DispatcherLeaderService leaderService;
    private final JudgeTaskRetryService retryService;

    @RabbitListener(queues = "${judgemesh.dispatcher.mq.submitQueue:submit.queue}")
    public void onMessage(String payload) {
        if (!leaderService.isLeader()) {
            throw new IllegalStateException("follower dispatcher should not consume judge task");
        }
        JudgeTask task = null;
        try {
            task = objectMapper.readValue(payload, JudgeTask.class);
            dispatchService.dispatch(task);
        } catch (Exception ex) {
            log.warn("failed to dispatch judge task payload={}", payload, ex);
            if (task == null || ex instanceof com.fasterxml.jackson.core.JsonProcessingException) {
                try {
                    retryService.deadLetterRaw(payload, ex);
                    return;
                } catch (Exception retryEx) {
                    throw new IllegalStateException("invalid judge task payload", retryEx);
                }
            }
            try {
                retryService.retryOrDeadLetter(task, ex);
            } catch (Exception retryEx) {
                throw new IllegalStateException("dispatch retry failed", retryEx);
            }
        }
    }
}
