package com.qqq.rabbitmq_producer;

import com.qqq.rabbitmq_producer.service.ProducerService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SpringBootTest
class RabbitmqProducerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void createSimpleQueue(){
        String queueName = "qqqQueue";

        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置主机地址
        connectionFactory.setHost("127.0.0.1");
        //设置端口号
        connectionFactory.setPort(5672);
        //设置虚拟主机
        connectionFactory.setVirtualHost("/qqq");
        //连接用户
        connectionFactory.setUsername("qqq");
        //输入用户密码
        connectionFactory.setPassword("qqq");
        Connection connection = null;
        Channel channel = null;
        try {
            //创建连接
            connection = connectionFactory.newConnection();
            //创建频道
            channel = connection.createChannel();
            /**
             * 参数一：队列名称
             * 参数二：是否定义持久化队列
             * 参数三：是否独占本次连接
             * 参数四：是否在不使用的时候自动删除队列
             * 参数五：队列其他参数
             */
            channel.queueDeclare(queueName,true,false,false,null);
            String message = "hello qqq";
            channel.basicPublish("",queueName,null,message.getBytes());
            System.out.println("已发送消息" + message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

        }
    }

    @Autowired
    private ProducerService producerService;
    @Test
    public void simpleSendWithTemplate(){
        producerService.simpleSend("qqq 123");
    }
}
