package com.judgemesh.dispatcher.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DispatcherRabbitConfig {

    private final DispatcherProperties properties;

    @Bean
    public Queue submitQueue() {
        return new Queue(properties.getMq().getSubmitQueue(), true);
    }

    @Bean
    public Queue submitDeadLetterQueue() {
        return new Queue(properties.getMq().getDeadLetterQueue(), true);
    }
}
