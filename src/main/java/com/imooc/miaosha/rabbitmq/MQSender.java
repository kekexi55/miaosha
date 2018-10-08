package com.imooc.miaosha.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMiaoshaMessage(MiaoshaMessage message){
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE,message);
    }
}
