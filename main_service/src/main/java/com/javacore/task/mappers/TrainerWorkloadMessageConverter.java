package com.javacore.task.mappers;

import jakarta.jms.BytesMessage;
import jakarta.jms.JMSException;

import jakarta.jms.Message;
import jakarta.jms.Session;
import org.jetbrains.annotations.NotNull;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class TrainerWorkloadMessageConverter implements MessageConverter {

    @Override
    public @NotNull Message toMessage(@NotNull Object object, @NotNull Session session) throws JMSException {
        if (object instanceof String) {
            String jsonString = (String) object;
            byte[] bytes = jsonString.getBytes(StandardCharsets.UTF_8);
            BytesMessage bytesMessage = session.createBytesMessage();
            bytesMessage.writeBytes(bytes);
            return bytesMessage;
        } else {
            throw new IllegalArgumentException("Expected object of type String, but received: " + object.getClass().getName());
        }
    }

    @Override
    public @NotNull Object fromMessage(@NotNull Message message) throws JMSException {
        BytesMessage bytesMessage = (BytesMessage) message;
        byte[] bytes = new byte[(int) bytesMessage.getBodyLength()];
        bytesMessage.readBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
