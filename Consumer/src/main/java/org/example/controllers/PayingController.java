package org.example.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.NewPaymentDto;
import org.example.dto.PaymentDTO;
import org.example.entity.primary.Booking;
import org.example.repository.primary.BookingRepository;
import org.example.service.TransactionService.PaymentTransactionService;
import org.example.service.YookassaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/paying")
public class PayingController {

    // post - pay метод - гость оплачивает бронирование
    private final PaymentTransactionService paymentTransactionService;
    private final YookassaService yookassaService;
    private final BookingRepository bookingRepository;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/pay")
    public ResponseEntity<?> payForBooking(@RequestBody PaymentDTO paymentDTO) {
        try {

            Booking booking = bookingRepository.findById(paymentDTO.getBookingId())
                    .orElseThrow(() -> new IllegalArgumentException("Бронирование не найдено"));


            NewPaymentDto newPayment = yookassaService.createNewPaymentForOffer(booking);


            PaymentDTO savedPayment = paymentTransactionService.savePayment(
                    paymentDTO.getBookingId(),
                    newPayment.getPaymentId(),
                    newPayment.getAmount()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Платеж успешно создан");
            response.put("payment_url", newPayment.getPaymentUrl());
            response.put("payment_id", newPayment.getPaymentId());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Ошибка при создании платежа: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
