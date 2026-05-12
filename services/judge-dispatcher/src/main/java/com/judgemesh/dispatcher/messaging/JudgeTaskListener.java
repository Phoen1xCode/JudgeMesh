package com.judgemesh.dispatcher.messaging;

import com.judgemesh.api.message.JudgeTask;
import com.judgemesh.dispatcher.service.DispatcherService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "judgemesh.dispatcher", name = "listener-enabled", havingValue = "true", matchIfMissing = true)
public class JudgeTaskListener {
    private final DispatcherService dispatcherService;

    public JudgeTaskListener(DispatcherService dispatcherService) {
        this.dispatcherService = dispatcherService;
    }

    @RabbitListener(queues = "${judgemesh.mq.submit-queue:submit.queue}")
    public void onTask(JudgeTask task) {
        dispatcherService.dispatch(task);
    }
}
