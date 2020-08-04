package com.qqq.activemq_consumer.listener;


import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

@Component
public class MsgListener {

    /**
     * 用于接收消息
     * destination:队列名称或者主题名称
     */
    @JmsListener(destination = "queueWithTr",containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(Message message, Session session){
        if(message instanceof TextMessage){
            TextMessage textMessage = (TextMessage)message;
            try {
                System.out.println("接收消息: " + textMessage.getText());
                textMessage.acknowledge();
            }catch (JMSException e){
                e.printStackTrace();
            }
        }
    }
}
