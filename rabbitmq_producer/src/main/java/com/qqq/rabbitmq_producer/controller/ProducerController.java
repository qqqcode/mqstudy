package com.qqq.rabbitmq_producer.controller;

import com.qqq.rabbitmq_producer.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {

    @Autowired
    private ProducerService producerService;

    @GetMapping("/topicSendMsg")
    public String topicSendMsg(@RequestParam  String msg,@RequestParam String key){
        return producerService.topicSendMsg(msg,key);
    }
}
