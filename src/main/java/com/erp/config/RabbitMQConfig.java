package com.erp.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "notification.queue";
    public static final String EXCHANGE_NAME = "notification.exchange";
    public static final String ROUTING_KEY = "notification.key";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true); // durable false 일 경우 휘발성 큐가 됨
    }

    @Bean
    public DirectExchange exchange() {
        //DirectExchange 생성
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        // 메시지가 notification.key 라우팅 키를 사용할 때 notification.queue로 전달되도록 설정
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
}
