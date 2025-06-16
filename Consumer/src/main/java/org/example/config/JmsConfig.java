package org.example.config;

import jakarta.jms.ConnectionFactory;
import org.example.dto.NotificationMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableJms
public class JmsConfig {
    @Bean
    public MappingJackson2MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        Map<String, Class<?>> typeIdMappings = new HashMap<>();
        typeIdMappings.put("notification", NotificationMessage.class);
        converter.setTypeIdMappings(typeIdMappings);
        return converter;
    }

    @Bean
    public DefaultJmsListenerContainerFactory  jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory jmsListenerContainerFactory=
                new DefaultJmsListenerContainerFactory();
        jmsListenerContainerFactory.setConnectionFactory(connectionFactory);
        jmsListenerContainerFactory.setConcurrency("5-10");
        jmsListenerContainerFactory.setMessageConverter(jacksonJmsMessageConverter());
        return jmsListenerContainerFactory;
    }
}
