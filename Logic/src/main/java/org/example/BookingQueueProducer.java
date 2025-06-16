package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.example.dto.NotificationMessage;
import org.fusesource.hawtbuf.AsciiBuffer;
import org.fusesource.stomp.client.BlockingConnection;
import org.fusesource.stomp.client.Stomp;
import org.fusesource.stomp.codec.StompFrame;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.net.URI;

@RequiredArgsConstructor
@Service
public class BookingQueueProducer {
    private BlockingConnection connection;

    @Value("${stomp.uri}")
    private String stompUri;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @PostConstruct
    public void init() throws Exception {
        Stomp stomp = new Stomp(new URI(stompUri));
        this.connection = stomp.connectBlocking();
    }

    public void send(String destination, String message) {
        try {
            StompFrame frame = new StompFrame(new AsciiBuffer("SEND"));
            frame.addHeader(new AsciiBuffer("destination"), new AsciiBuffer(destination));
            frame.addHeader(new AsciiBuffer("_type"), new AsciiBuffer("notification"));

            frame.content(new AsciiBuffer(message.getBytes()));
            connection.send(frame);
            System.out.println("Отправка сообщения по STOMP: " + message);
        } catch (Exception e) {
            throw new RuntimeException("STOMP отправка провалилась", e);
        }
    }
    public void sendNotification(NotificationMessage message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            send("bookingservice-queue", json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка сериализации уведомления", e);
        }
    }
    @PreDestroy
    public void cleanup() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}
