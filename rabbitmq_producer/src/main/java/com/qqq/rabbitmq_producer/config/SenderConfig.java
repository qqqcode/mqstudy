package com.qqq.rabbitmq_producer.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Johnson
 * 2020/8/7
 */
@Configuration
public class SenderConfig {
    @Bean
    public Queue queue(){
        return new Queue("qqqQueue");
    }
}
