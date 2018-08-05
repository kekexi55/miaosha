package com.imooc.miaosha.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {
    @Autowired
    private AmqpTemplate amqpTemplate;
    public void sendMessage(){
        amqpTemplate.convertAndSend(MQConfig.QName,"hello,world");
    }

    public void sendMiaoshaMessage(MiaoshaMessage message){
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE,message);
    }
}
