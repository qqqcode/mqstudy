package com.qqq.rabbitmq_consumer.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class QQQQueueListener {

    public static final String QQQ_QUEUE = "qqq_queue";

    @RabbitListener(queues = QQQ_QUEUE)
    public void receiveMsg(String msg){
        System.out.println("receiver msg : "+msg);
    }
}
