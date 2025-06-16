package org.example;

import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.NotificationMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingQueueConsumer {
    private final EmailService emailService;

    @JmsListener(destination = "bookingservice-queue", containerFactory = "jmsListenerContainerFactory")
    public void messageListener(NotificationMessage message) {
        log.info("Message received: {}", message);

        try {
            emailService.sendBookingNotification(
                    "alina.armun@gmail.com",
                    message.getBookingId().toString(),
                    message.getTitle(),
                    message.getDescription()
            );

        } catch (Exception e) {
            log.error("Ошибка при отправке уведомления", e);
        }

    }
}

