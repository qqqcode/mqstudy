package top.qqq.listener.asy;


import com.qqq.comment.pojo.UserLog;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import top.qqq.config.ArtemisConfig;

import javax.jms.*;


@Component
public class ConsumerAsyTest {


    @JmsListener(destination = "QqqAsyQueue",containerFactory = "jmsListenerContainerFactory",selector = "TOCONSUMER='B'")
    public void receiveMessage(Message message, Session session) throws JMSException {
        String toconsumer = message.getStringProperty("TOCONSUMER");
        System.out.print(toconsumer+">>");
        if(message instanceof ObjectMessage){
            ObjectMessage objectMessage = (ObjectMessage)message;
            try {
                UserLog userLog =(UserLog) objectMessage.getObject();
                System.out.println(userLog.toString());
            }catch (JMSException e){
                e.printStackTrace();
            }
        }
    }

    @JmsListener(destination = "QqqAsyTopic",containerFactory = "jmsListenerContainerFactory")
    public void receiveTopicMessage(Message message, Session session){
        if(message instanceof ObjectMessage){
            ObjectMessage objectMessage = (ObjectMessage)message;
            try {
                UserLog userLog =(UserLog) objectMessage.getObject();
                System.out.println(userLog.toString());
            }catch (JMSException e){
                e.printStackTrace();
            }
        }
    }

    @JmsListener(destination = ArtemisConfig.ASYQUEUE,containerFactory = "jmsListenerContainerQueue",selector = "TOCONSUMER='A'")
    public void getAsyQueueMessage(Message message, Session session) throws JMSException {
        String toconsumer = message.getStringProperty("TOCONSUMER");
        System.out.print(toconsumer+">>>>");
        if(message instanceof ObjectMessage){
            ObjectMessage objectMessage = (ObjectMessage)message;
            try {
                UserLog userLog =(UserLog) objectMessage.getObject();
                System.out.println(userLog.toString());
            }catch (JMSException e){
                e.printStackTrace();
            }
        }
    }

    @JmsListener(destination = ArtemisConfig.ASYTOPIC,containerFactory = "jmsListenerContainerTopic")
    public void getAsTopocMessage(Message message, Session session) throws JMSException {
        String toconsumer = message.getStringProperty("TOCONSUMER");
        System.out.print(toconsumer+">>>>");
        if(message instanceof ObjectMessage){
            ObjectMessage objectMessage = (ObjectMessage)message;
            try {
                UserLog userLog =(UserLog) objectMessage.getObject();
                System.out.println(userLog.toString());
            }catch (JMSException e){
                e.printStackTrace();
            }
        }
    }


    @JmsListener(destination = ArtemisConfig.ACKQUEUE,containerFactory = "jmsListenerContainerAcknowledge")
    public void getAckMessage(Message message, Session session) throws JMSException {
        if(message instanceof ObjectMessage){
            ObjectMessage objectMessage = (ObjectMessage)message;
            try {
                UserLog userLog =(UserLog) objectMessage.getObject();
                System.out.println(userLog.toString());
                //objectMessage.acknowledge();
                session.recover();
            }catch (JMSException e){
                e.printStackTrace();
            }
        }
    }
}
