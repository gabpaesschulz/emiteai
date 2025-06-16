package com.emiteai.api.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_RELATORIO = "relatorio.ex";
    public static final String QUEUE_RELATORIO    = "relatorio.q";

    @Bean
    FanoutExchange relatorioExchange() {
        return new FanoutExchange(EXCHANGE_RELATORIO, true, false);
    }

    @Bean
    Queue relatorioQueue() {
        return new Queue(QUEUE_RELATORIO, true);
    }

    @Bean
    Binding bind() {
        return BindingBuilder.bind(relatorioQueue())
                .to(relatorioExchange());
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory cf) {
        return new RabbitTemplate(cf);
    }
}
