package com.imooc.miaosha.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
    @Bean
    public Queue getQueue(){
        return new Queue(MQConfig.QName);
    }
    public static final String QName="queue";
    public static final String MIAOSHA_QUEUE="miaosha.queue";
}
