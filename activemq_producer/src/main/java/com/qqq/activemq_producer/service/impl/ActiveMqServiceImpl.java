package com.qqq.activemq_producer.service.impl;

import com.qqq.activemq_producer.service.ActiveMqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActiveMqServiceImpl implements ActiveMqService {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Override
    public void ptpSender(String message) {
        jmsMessagingTemplate.convertAndSend("qqqQueue1",message);
    }

//    对消息发送加入到事务管理，同时也对jdbc数据库事务生效
    @Transactional
    public void sendMessageWithTr(){
        for (int i=0;i<10;i++){
            jmsMessagingTemplate.convertAndSend("queueWithTr","消息"+i);
        }
    }
}
