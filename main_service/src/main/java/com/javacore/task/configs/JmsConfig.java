package com.javacore.task.configs;

import com.javacore.task.mappers.TrainerWorkloadMessageConverter;
import jakarta.jms.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import jakarta.jms.Queue;
import org.springframework.jms.core.JmsTemplate;



@Configuration
@Slf4j
@EnableJms
@RequiredArgsConstructor
public class JmsConfig {

    private final TrainerWorkloadMessageConverter messageConverter;

    @Bean
    public JmsTemplate jmsTemplate(@Qualifier("jmsConnectionFactory") ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory);
        jmsTemplate.setMessageConverter(messageConverter);
        return jmsTemplate;
    }

    @Bean
    public Queue queue(){
        return new ActiveMQQueue("trainerWorkloadQueue");
    }

}