package com.qqq.activemq_producer.Schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ProducerSchedule {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Scheduled(fixedDelay = 3000)
    public void sendMsg(){
        jmsMessagingTemplate.convertAndSend("qqqQueue","定时发送的消息");
    }
}
