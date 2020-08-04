package com.qqq.activemq_producer.service;

public interface ActiveMqService {
    void ptpSender(String message);
    void sendMessageWithTr();
}
