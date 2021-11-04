package top.qqq.asy;

import com.qqq.comment.pojo.UserLog;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.apache.activemq.artemis.jms.client.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

@RestController
@RequestMapping("/asy/")
public class AsyTest {

    @Value("${mqcofig.asyqueue}")
    String asyQueue = "QqqAsyQueue";


    @Autowired
    private JmsTemplate jmsTemplate;


    @RequestMapping("sendEmail")
    public String toSendEmail(@RequestBody String emailAddress) {
        return "success";
    }

    @RequestMapping("loginAndSaveLog")
    public String loginAndSaveLog() {
        UserLog userLog = UserLog.builder().logName("用户登录").logType("login").userName("qqq").build();
        for (int i = 0; i < 10; i++) {
            if (i%2 == 0) {
                sendPTP(userLog,"A");
                sendTOPIC(userLog,"ALL");
            } else {
                sendPTP(userLog,"B");
                sendTOPIC(userLog,"ALL");
            }
        }
        return "success";
    }

    @RequestMapping("ackMessage")
    public String ackMessage() {
        UserLog userLog = UserLog.builder().logName("用户确认").logType("ack").userName("qqq").build();
        for (int i = 0; i < 10; i++) {
            int temp = i;
            jmsTemplate.send(new ActiveMQQueue("QqqAckQueue"),session -> {
                ObjectMessage message = session.createObjectMessage(userLog);
                message.setIntProperty("times",temp);
                return message;
            });
        }
        return "success";
    }

    private void sendPTP(UserLog o,String to){
        jmsTemplate.send(new ActiveMQQueue("QqqAsyQueue"), session -> {
            ObjectMessage message = session.createObjectMessage(o);
            message.setStringProperty("TOCONSUMER",to);
            message.setJMSExpiration(System.currentTimeMillis() + 5000);
            message.setLongProperty("_AMQ_SCHED_DELIVERY", System.currentTimeMillis() + 5000);
            return message;
        });
    }

    private void sendTOPIC(UserLog o,String to){
        jmsTemplate.send(new ActiveMQTopic("QqqAsyTopic"), session -> {
            ObjectMessage message = session.createObjectMessage(o);
            message.setStringProperty("TOCONSUMER",to);
            return message;
        });
    }


}
