package com.qqq.rabbitmq_producer.service.impl;

import com.qqq.rabbitmq_producer.config.RabbitMQConfig;
import com.qqq.rabbitmq_producer.service.ProducerService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Johnson
 * 2020/8/7
 */
@Service
public class ProducerServiceImpl implements ProducerService {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void simpleSend(String string) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.TTL_QUEUE,string);
        System.out.println("send success" + string);
    }

    @Override
    public String topicSendMsg(String msg, String key) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE,key,msg);
        return "success send!!";
    }
}
