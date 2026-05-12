package com.judgemesh.dispatcher.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitTopologyConfig {

    @Bean
    Queue submitQueue(@Value("${judgemesh.mq.submit-queue:submit.queue}") String queueName) {
        return QueueBuilder.durable(queueName).build();
    }
}
