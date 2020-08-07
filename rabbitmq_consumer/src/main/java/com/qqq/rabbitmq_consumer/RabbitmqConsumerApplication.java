package com.qqq.rabbitmq_consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitmqConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqConsumerApplication.class, args);
    }

    @RabbitListener(queues = "qqqQueue")
    public void receiveMsg(String msg){
        System.out.println(">>>>>>>>>>>>>>>>>");
        System.out.println(msg);
    }

}
