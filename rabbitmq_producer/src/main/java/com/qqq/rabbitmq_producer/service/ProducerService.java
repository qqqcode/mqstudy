package com.qqq.rabbitmq_producer.service;

/**
 * @author Johnson
 * 2020/8/7
 */
public interface ProducerService {
    void simpleSend(String string);

    String topicSendMsg(String msg,String key);
}
