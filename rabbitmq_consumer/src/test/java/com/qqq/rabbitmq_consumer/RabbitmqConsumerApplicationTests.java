package com.qqq.rabbitmq_consumer;

import com.rabbitmq.client.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SpringBootTest
class RabbitmqConsumerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void receiveMsg(){
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置主机地址
        connectionFactory.setHost("127.0.0.1");
        //连接端口
        connectionFactory.setPort(5672);
        //设置虚拟主机名称
        connectionFactory.setVirtualHost("/qqq");
        //连接用户名
        connectionFactory.setUsername("qqq");
        //连接密码
        connectionFactory.setPassword("qqq");
        //创建连接
        Connection connection = null;
        Channel channel = null;
        try {
            connection = connectionFactory.newConnection();
            //创建频道
            channel = connection.createChannel();
            channel.queueDeclare("qqqQueue",true,false,false,null);
            DefaultConsumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    //路由key
                    System.out.println("路由key为：" + envelope.getRoutingKey());
                    //交换机
                    System.out.println("交换机为：" + envelope.getExchange());
                    //消息id
                    System.out.println("消息id为：" + envelope.getDeliveryTag());
                    //收到的消息
                    System.out.println("接收到的消息为：" + new String(body,"UTF-8"));
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                }
            };
            channel.basicConsume("qqqQueue",true,consumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public ConnectionFactory getConnectionFactory(){
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置主机地址
        connectionFactory.setHost("127.0.0.1");
        //连接端口
        connectionFactory.setPort(5672);
        //设置虚拟主机名称
        connectionFactory.setVirtualHost("/qqq");
        //连接用户名
        connectionFactory.setUsername("qqq");
        //连接密码
        connectionFactory.setPassword("qqq");
        return connectionFactory;
    }
    static final String FANOUT_EXCHANGE = "fanout_exchange";
    static final String FANOUT_QUEUE1 = "fanout_queue_1";
    static final String FANOUT_QUEUE2 = "fanout_queue_2";
    @Test
    public void pubSubConsumer(){
        try {
            Connection connection = getConnectionFactory().newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(FANOUT_QUEUE2,true,false,false,null);
            channel.queueBind(FANOUT_QUEUE2,FANOUT_EXCHANGE,"");
            DefaultConsumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    //路由key
                    System.out.println("路由key为：" + envelope.getRoutingKey());
                    //交换机
                    System.out.println("交换机为：" + envelope.getExchange());
                    //消息id
                    System.out.println("消息id为：" + envelope.getDeliveryTag());
                    //收到的消息
                    System.out.println("接收到的消息为：" + new String(body,"UTF-8"));
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                }
            };
            channel.basicConsume(FANOUT_QUEUE2,true,consumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    static final String DIRERCT_EXCHANGE = "direct_exchange";
    static final String DIRECT_QUEUE_INSERT = "direct_queue_insert";
    static final String DIRECT_QUEUE_UPDATE = "direct_queue_update";

    @Test
    public void routingConsumer(){
        try {
            Connection connection = getConnectionFactory().newConnection();
            Channel channel = connection.createChannel();
            //声明交换机
            channel.exchangeDeclare(DIRERCT_EXCHANGE,BuiltinExchangeType.DIRECT);
            //创建队列
            channel.queueDeclare(DIRECT_QUEUE_INSERT,true,false,false,null);
            //队列绑定交换机
            channel.queueBind(DIRECT_QUEUE_INSERT,DIRERCT_EXCHANGE,"insert");
            DefaultConsumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    //路由key
                    System.out.println("路由key为：" + envelope.getRoutingKey());
                    //交换机
                    System.out.println("交换机为：" + envelope.getExchange());
                    //消息id
                    System.out.println("消息id为：" + envelope.getDeliveryTag());
                    //收到的消息
                    System.out.println("接收到的消息为：" + new String(body,"UTF-8"));
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                }
            };
            channel.basicConsume(DIRECT_QUEUE_INSERT,true,consumer);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    static final String TOPIC_EXCHANGE = "topic_exchange";
    static final String TOPIC_QUEUE_1 = "topic_queue_1";
    static final String TOPIC_QUEUE_2 = "topic_queue_2";

    @Test
    public void topicConsumer(){
        try {
            Connection connection = getConnectionFactory().newConnection();
            Channel channel = connection.createChannel();
            //声明交换机
            channel.exchangeDeclare(TOPIC_EXCHANGE,BuiltinExchangeType.TOPIC);
            //创建队列
            channel.queueDeclare(TOPIC_QUEUE_1,true,false,false,null);
            //队列绑定交换机
            channel.queueBind(TOPIC_QUEUE_1,TOPIC_EXCHANGE,"#.insert");
            channel.queueBind(TOPIC_QUEUE_1,TOPIC_EXCHANGE,"*.delete");

            DefaultConsumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    //路由key
                    System.out.println("路由key为：" + envelope.getRoutingKey());
                    //交换机
                    System.out.println("交换机为：" + envelope.getExchange());
                    //消息id
                    System.out.println("消息id为：" + envelope.getDeliveryTag());
                    //收到的消息
                    System.out.println("接收到的消息为：" + new String(body,"UTF-8"));
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                }
            };
            channel.basicConsume(TOPIC_QUEUE_1,true,consumer);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }

}
