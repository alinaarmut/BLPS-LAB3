package org.example;



import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendBookingNotification(String toEmail, String bookingId, String title, String description) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("alina.armun@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Новое бронирование #" + bookingId);
        message.setText("Здравствуйте! У вас новое бронирование  " + title +
                ". Номер бронирования: " + bookingId + "Описание: " + description);

        mailSender.send(message);
    }
}
