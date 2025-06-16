package org.example.service.TransactionService;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PaymentDTO;
import org.example.entity.enums_status.BookingStatus;
import org.example.entity.enums_status.OfferStatus;
import org.example.entity.enums_status.PaymentStatus;
import org.example.entity.primary.Booking;
import org.example.entity.primary.Payment;
import org.example.entity.secondary.Offer;
import org.example.repository.primary.BookingRepository;
import org.example.repository.primary.PaymentRepository;
import org.example.repository.secondary.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentTransactionService {
    private final PaymentRepository paymentRepository;
    private final OfferRepository offerRepository;
    private final BookingRepository bookingRepository;
    public PaymentDTO savePayment(Long bookingId, UUID paymentId, Double amount) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Бронирование не найдено"));

        Payment payment = new Payment();
        payment.setBookingId(bookingId);
        payment.setPaymentId(paymentId);
        payment.setAmount(amount);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);
        log.info("Создан платеж {} для бронирования {}", paymentId, bookingId);

        return convertToDto(savedPayment);
    }

    public void updatePaymentStatus(UUID paymentId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> {
                    log.error("Платеж {} не найден", paymentId);
                    return new IllegalArgumentException("Платеж не найден");
                });

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            log.warn("Платеж {} уже подтвержден", paymentId);
            return;
        }


        payment.setStatus(PaymentStatus.SUCCESS);
        log.info("СТАТУС ПЛАТЕЖА ИЗМЕНЕН НА success");
        paymentRepository.save(payment);


        Offer offer = offerRepository.findByBookingId(payment.getBookingId())
                .orElseThrow(() -> {
                    log.error("Оффер для бронирования {} не найден", payment.getBookingId());
                    return new IllegalStateException("Оффер не найден");
                });

        if (offer.getStatus() != OfferStatus.PAYED) {
            offer.setStatus(OfferStatus.PAYED);
            offer.setConfirmedAt(LocalDateTime.now());
            offerRepository.save(offer);
            log.info("Статус оффера {} обновлен на PAYED", offer.getId());
        } else {
            log.warn("Оффер {} уже имеет статус PAYED", offer.getId());
        }
        Booking booking = bookingRepository.findById(payment.getBookingId())
                .orElseThrow(() -> {
                    log.error("Бронирование {} не найдено", payment.getBookingId());
                    return new IllegalStateException("Бронирование не найдено");
                });

        if (booking.getBookingStatus() != BookingStatus.PAYED) {
            booking.setBookingStatus(BookingStatus.PAYED);
            bookingRepository.save(booking);
            log.info("Статус бронирования {} обновлен на PAYED", booking.getId());
        } else {
            log.warn("Бронирование {} уже имеет статус CONFIRMED", booking.getId());
        }


        log.info("Платеж {} подтвержден", paymentId);
    }

    public void cancelPayment(UUID paymentId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Платеж не найден"));

        payment.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);

        log.info("Платеж {} отменен", paymentId);
    }

    private PaymentDTO convertToDto(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setBookingId(payment.getBookingId());
        dto.setPaymentId(payment.getPaymentId());
        dto.setAmount(payment.getAmount());
        dto.setSuccess(payment.getStatus() == PaymentStatus.SUCCESS);
        return dto;
    }

}
