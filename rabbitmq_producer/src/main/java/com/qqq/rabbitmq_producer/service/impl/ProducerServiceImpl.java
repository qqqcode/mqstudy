package com.qqq.rabbitmq_producer.service.impl;

import com.qqq.rabbitmq_producer.service.ProducerService;
import org.springframework.amqp.core.AmqpTemplate;
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

    @Override
    public void simpleSend(String string) {
        amqpTemplate.convertAndSend("qqqQueue",string);
    }
}
