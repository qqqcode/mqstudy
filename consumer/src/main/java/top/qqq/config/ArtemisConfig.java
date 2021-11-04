package top.qqq.config;


import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.apache.activemq.artemis.jms.client.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;

@Configuration
public class ArtemisConfig {

    public final static String ASYQUEUE = "QqqAsyQueue";

    public final static String ACKQUEUE = "QqqAckQueue";

    public final static String ASYTOPIC = "QqqAsyTopic";

    @Bean
    public Queue asyQueue() {
        return new ActiveMQQueue(ASYQUEUE);
    }

    @Bean
    public Topic asyTopic() {
        return new ActiveMQTopic(ASYTOPIC);
    }

    @Bean
    public Queue ackQueue() {
        return new ActiveMQQueue(ACKQUEUE);
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerQueue(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setConnectionFactory(connectionFactory);
        return bean;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerTopic(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setPubSubDomain(true);
        bean.setConnectionFactory(connectionFactory);
        return bean;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerAcknowledge(ConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setSessionTransacted(false);
        //自动确认
        //factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        //手动确认
        factory.setSessionAcknowledgeMode(4);
        return factory;
    }
}

