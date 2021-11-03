package com.qqq.activemq_producer;

import com.qqq.activemq_producer.service.ActiveMqService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.TestPropertySource;

import javax.jms.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootTest
class ActivemqProducerApplicationTests {

    @Autowired
    ActiveMqService activeMqService;

    @Test
    void contextLoads() {
    }

    @Test
    public void test01(){
        activeMqService.ptpSender("qqq nice ptp!");
    }

    @Test
    @JmsListener(destination = "qqqQueue1",containerFactory = "jmsListenerContainerFactory")
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

    @Autowired
    JmsTemplate jmsTemplate;
    @Test
    public void sendTextMessage(){
        jmsTemplate.send("qqqQueue", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage("qqq nice");
                return message;
            }
        });
    }

    @Test
    public void sendMapMessage(){
        jmsTemplate.send("qqqQueue", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                StreamMessage message = session.createStreamMessage();
                message.writeString("qqqq");
                message.writeInt(20);
                message.setStringProperty("property","qqq");
                return message;
            }
        });
    }


    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Test
    public void testMessage() throws Exception{
        ConnectionFactory connectionFactory = jmsMessagingTemplate.getConnectionFactory();
        Session session = null;
        try {
            Connection con= connectionFactory.createConnection();

            session = con.createSession(true, Session.AUTO_ACKNOWLEDGE);
            MessageProducer qqqQueueWithTx = session.createProducer(session.createQueue("qqqQueueWithTx"));

            for(int i=0;i<10;i++){
                TextMessage textMessage = session.createTextMessage("mes : " + i);
                //textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,10000);
                qqqQueueWithTx.send(textMessage);
            }

            session.commit();
        } catch (JMSException e) {
            e.printStackTrace();
            session.rollback();
        }
    }

    @Test
    public void sendMesWithTr(){
        activeMqService.sendMessageWithTr();
    }

    @Test
    public void sendMsgByAsync(){
//        //连接配置
//        new ActiveMQConnectionFactory("tcp://localhost:61616?jms.useAsyncSend=true");
//        //ConnectionFactory
//        ((ActiveMQConnectionFactory)connectionFactory).setUseAsyncSend(true);
//        //connection
//        ((ActiveMQConnection)connection).setUseAsyncSend(true);
    }
}
