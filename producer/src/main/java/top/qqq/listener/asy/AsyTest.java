package top.qqq.listener.asy;

import com.qqq.comment.pojo.UserLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
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
                jmsTemplate.convertAndSend((MessageCreator) session -> {
                    ObjectMessage message = session.createObjectMessage(userLog);
                    message.setStringProperty("TOCONSUMER","A");
                    return message;
                });
            } else {
                jmsTemplate.convertAndSend((MessageCreator) session -> {
                    ObjectMessage message = session.createObjectMessage(userLog);
                    message.setStringProperty("TOCONSUMER","B");
                    return message;
                });
            }
        }
        return "success";
    }

}
