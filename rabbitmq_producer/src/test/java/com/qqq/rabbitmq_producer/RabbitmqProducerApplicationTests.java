package com.qqq.rabbitmq_producer;

import com.qqq.rabbitmq_producer.service.ProducerService;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeAll;
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
        producerService.simpleSend("qqq deletle");
    }

    public static String QUEUE_NAME = "work_queue";

    public ConnectionFactory getConnectionFactory(){
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
        return connectionFactory;
    }

    @Test
    public void workQueue(){
        Connection connection = null;
        Channel channel = null;
        try {
            connection = getConnectionFactory().newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME,true,false,false,null);
            for( int i = 0;i <= 20;i++){
                String message = "this is " + i + "times";
                channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
                System.out.println("send : "+ message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
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

    static final String FANOUT_EXCHANGE = "fanout_exchange";
    static final String FANOUT_QUEUE1 = "fanout_queue_1";
    static final String FANOUT_QUEUE2 = "fanout_queue_2";

    @Test
    public void subPubProducer(){
        try {
            Connection connection = getConnectionFactory().newConnection();
            Channel channel = connection.createChannel();
            /**
             * 参数一：交换机名称
             * 交换机类型
             */
            channel.exchangeDeclare(FANOUT_EXCHANGE, BuiltinExchangeType.FANOUT);
            /**
             * 参数一：队列名称
             * 参数二：是否定义持久化队列
             * 参数三：是否独占本次连接
             * 参数四：是否在不使用的时候自动删除队列
             * 参数五：队列其他参数
             */
            channel.queueDeclare(FANOUT_QUEUE1,true,false,false,null);
            channel.queueDeclare(FANOUT_QUEUE2,true,false,false,null);
            //队列绑定交换机
            channel.queueBind(FANOUT_QUEUE1,FANOUT_EXCHANGE,"");
            channel.queueBind(FANOUT_QUEUE2,FANOUT_EXCHANGE,"");
            for (int i=0;i<10;i++){
                String msg = "this is "+i+" times";
                channel.basicPublish(FANOUT_EXCHANGE,"",null,msg.getBytes());
                System.out.println("send mes" + i);
            }
            channel.close();
            connection.close();
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
    public void routing(){
        try {
            Connection connection = getConnectionFactory().newConnection();
            Channel channel = connection.createChannel();
            /**
             * 参数一：交换机名称
             * 交换机类型
             */
            channel.exchangeDeclare(DIRERCT_EXCHANGE, BuiltinExchangeType.DIRECT);
            /**
             * 参数一：队列名称
             * 参数二：是否定义持久化队列
             * 参数三：是否独占本次连接
             * 参数四：是否在不使用的时候自动删除队列
             * 参数五：队列其他参数
             */
            channel.queueDeclare(DIRECT_QUEUE_INSERT,true,false,false,null);
            channel.queueDeclare(DIRECT_QUEUE_UPDATE,true,false,false,null);
            //队列绑定交换机
            channel.queueBind(DIRECT_QUEUE_INSERT,DIRERCT_EXCHANGE,"insert");
            channel.queueBind(DIRECT_QUEUE_UPDATE,DIRERCT_EXCHANGE,"update");
            //发送消息
            String message = "路由发送，key为insert";
            /**
             * 参数一：交换机名称
             * 参数二：路由key，简单模式可以传递队列名称
             * 参数三：消息其他属性
             * 参数四：消息内容
             */
            channel.basicPublish(DIRERCT_EXCHANGE,"insert",null,message.getBytes());
            System.out.println("send message : " + message);

            message = "路由发送，key为update";
            channel.basicPublish(DIRERCT_EXCHANGE,"update",null,message.getBytes());

            channel.close();
            connection.close();

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
    public void topicProducer(){
        try {
            Connection connection = getConnectionFactory().newConnection();
            Channel channel = connection.createChannel();
            /**
             * 参数一：交换机名称
             * 交换机类型
             */
            channel.exchangeDeclare(TOPIC_EXCHANGE, BuiltinExchangeType.TOPIC);

            //发送消息
            String message = "路由发送，key为item.insert";
            /**
             * 参数一：交换机名称
             * 参数二：路由key，简单模式可以传递队列名称
             * 参数三：消息其他属性
             * 参数四：消息内容
             */
            channel.basicPublish(TOPIC_EXCHANGE,"item.insert",null,message.getBytes());
            System.out.println("send message : " + message);

            message = "路由发送，key为item.update";
            channel.basicPublish(TOPIC_EXCHANGE,"item.update",null,message.getBytes());

            message = "路由发送，key为item.delete";
            channel.basicPublish(TOPIC_EXCHANGE,"item.delete",null,message.getBytes());

            channel.close();
            connection.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }

}
