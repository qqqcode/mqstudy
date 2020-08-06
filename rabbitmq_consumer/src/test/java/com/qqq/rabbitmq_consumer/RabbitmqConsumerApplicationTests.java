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
}
