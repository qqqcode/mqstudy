package com.qqq.rabbitmq_producer.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String TOPIC_EXCHANGE = "topic_exchange";
    public static final String DLX_EXCHANGE = "dlx_exchange";

    public static final String QQQ_QUEUE = "qqq_queue";

    public static final String TTL_QUEUE = "ttl_queue";
    public static final String DLX_QUEUE = "dlx_queue";

    //
    @Bean("topicExchange")
    public Exchange topicExchange(){
        return ExchangeBuilder.topicExchange(TOPIC_EXCHANGE).durable(true).build();
    }

    @Bean("qqqQueue")
    public Queue qqqQueue(){
        return QueueBuilder.durable(QQQ_QUEUE).build();
    }

    @Bean
    public Binding QueueExchange(@Qualifier("qqqQueue") Queue queue,
                                 @Qualifier("topicExchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("item.#").noargs();
    }

    @Bean("ttl_queue")
    public Queue ttlQueue(){
        Queue ttl_queue = QueueBuilder.durable(TTL_QUEUE).withArgument("x-message-ttl",5000).build();
        return ttl_queue;
    }

    @Bean
    public Queue dlxQueue(){
        Queue dlxQueue = QueueBuilder.durable(DLX_QUEUE).build();
        return dlxQueue;
    }

    @Bean
    public Exchange dlxExchange(){
        Map<String,Object> argMap = new HashMap<>();
        Exchange dlxExchange = ExchangeBuilder.directExchange(DLX_EXCHANGE).build();
        return dlxExchange;
    }

}
