package com.qqq.activemq;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qqq.activemq.mapper.UserMapper;
import com.qqq.activemq.pojo.User;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.jms.*;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class ActivemqApplicationTests {

    @Autowired
    private UserMapper usermapper;
    @Test
    void contextLoads() {
        List<User> users = usermapper.selectList(null);
        users.forEach(System.out::println);
    }

    @Test
    public void testInsert(){
        User user = new User();
        user.setName("qqe");
        user.setAge(24);
        user.setEmail("qqqcode.qq.com");
        int res = usermapper.insert(user);
        System.out.println(res);
        System.out.println(user);
    }

    @Test
    public void testUpdate(){
        User user = new User();
        user.setName("qqr");
        user.setAge(24);
        user.setEmail("qqqcode.qq.com");
        user.setId(6);
        int res = usermapper.updateById(user);
        System.out.println(res);
        System.out.println(user);
    }

    @Test
    public void TestOptimisticLocker(){
        User user = usermapper.selectById(6);
        user.setName("qqa");
        usermapper.updateById(user);
    }

    @Test
    public void testSelectById(){
        List<User> users = usermapper.selectBatchIds(Arrays.asList(1, 2, 3));
        users.forEach(System.out::println);
    }

    @Test
    public void testPage(){
        Page<User> page = new Page<>(1,2);
        usermapper.selectPage(page,null);
        page.getRecords().forEach(System.out::println);
    }
    @Test
    public void testDelete(){
        int i = usermapper.deleteById(6);
    }

    @Test
    public void P2PproducerTest() throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
        Connection con = factory.createConnection();
        con.start();
        Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue qqqQueue = session.createQueue("qqqQueue");
        MessageProducer producer = session.createProducer(qqqQueue);
        TextMessage textMessage = session.createTextMessage("test message");
        producer.send(textMessage);

        System.out.println("消息发送完成");
        session.close();
        con.close();
    }

    @Test
    public void P2Pconsumer1() throws JMSException{
        //建立连接工厂
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
        //创建连接
        Connection con = factory.createConnection();
        //打开链接
        con.start();
        /**创建会话
         * 参数一：是否开启事务
         * 消息确认机制
         */
        Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建目标地址（Queue or Topic）
        Topic qqqTopic = session.createTopic("qqqTopic");
        //创建消费者
        MessageConsumer consumer = session.createConsumer(qqqTopic);
        //接收消息
        while (true){
            Message message = consumer.receive();
            if(message == null){
                break;
            }
            if(message instanceof  TextMessage){
                TextMessage textMessage = (TextMessage)message;
                System.out.println("接收消息： " + textMessage.getText());
            }
        }
        //释放资源
        session.close();
        con.close();
    }

    @Test
    public void P2PconsumerListener() throws JMSException{

        //建立连接工厂
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
        //创建连接
        Connection con = factory.createConnection();
        //打开链接
        con.start();
        /**创建会话
         * 参数一：是否开启事务
         * 参数二：消息确认机制
         */
        Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建目标地址（Queue or Topic）
        Queue qqqQueue = session.createQueue("qqqQueue");
        //创建消费者
        MessageConsumer consumer = session.createConsumer(qqqQueue);
        //设置监听器来接收消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if(message instanceof TextMessage){
                    TextMessage textMessage = (TextMessage)message;
                    try {
                        System.out.println("监听者接收消息" + textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //监听器下不能释放连接，一旦关闭，消息无法接收
    }

    @Test
    public void SubPubProducer() throws JMSException{
        //建立连接工厂
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
        //创建连接
        Connection con = factory.createConnection();
        //打开链接
        con.start();
        /**创建会话
         * 参数一：是否开启事务
         * 消息确认机制
         */
        Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建目标地址（Queue or Topic）
        Topic qqqTopic = session.createTopic("qqqTopic");
        //创建生产者
        MessageProducer producer = session.createProducer(qqqTopic);
        //创建消息
        TextMessage textMessage = session.createTextMessage("test topic message");
        //发送消息
        producer.send(textMessage);
        System.out.println("消息发送完成");
        //释放资源
        session.close();
        con.close();
    }

    @Test
    public void PubSubConsumer() throws JMSException{
        //建立连接工厂
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
        //创建连接
        Connection con = factory.createConnection();
        //打开链接
        con.start();
        /**创建会话
         * 参数一：是否开启事务
         * 参数二：消息确认机制
         */
        Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建目标地址（Queue or Topic）
        Topic qqqTopic = session.createTopic("qqqTopic");
        //创建消费者
        MessageConsumer consumer = session.createConsumer(qqqTopic);
        //设置监听器来接收消息
        consumer.setMessageListener(new MessageListener(){
            @Override
            public void onMessage(Message message) {
                if(message instanceof TextMessage){
                    TextMessage textMessage = (TextMessage)message;
                    try {
                        System.out.println("监听者接收消息" + textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //监听器下不能释放连接，一旦关闭，消息无法接收
    }
}
