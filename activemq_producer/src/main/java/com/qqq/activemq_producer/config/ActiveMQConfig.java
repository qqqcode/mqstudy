package com.qqq.activemq_producer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;

@Configuration
public class ActiveMQConfig {
//    @Bean
//    public PlatformTransactionManager createTransactionManager(ConnectionFactory connectionFactory){
//        return new JmsTransactionManager(connectionFactory);
//    }
//
//    /**
//     *
//     * @param connectionFactory
//     * @return
//     */
//    @Bean
//    @Autowired
//    public JmsTemplate asynJmsTemplate(ConnectionFactory connectionFactory){
//        JmsTemplate template = new JmsTemplate(connectionFactory);
//        template.setExplicitQosEnabled(true);
//        template.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
//        return template;
//    }
//
//    /**
//     *
//     * @param connectionFactory
//     * @return
//     */
//    @Bean
//    @Autowired
//    public JmsTemplate synJmsTemplate(ConnectionFactory connectionFactory){
//        JmsTemplate template= new JmsTemplate(connectionFactory);
//        return template;
//    }

}
