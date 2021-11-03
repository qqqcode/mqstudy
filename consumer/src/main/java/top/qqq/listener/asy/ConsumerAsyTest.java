package top.qqq.listener.asy;


import com.qqq.comment.pojo.UserLog;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.*;


@Component
public class ConsumerAsyTest {


    @JmsListener(destination = "QqqAsyQueue",containerFactory = "jmsListenerContainerFactory",selector = "TOCONSUMER='A'")
    public void receiveMessage(Message message, Session session){
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
}
