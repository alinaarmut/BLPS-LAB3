package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPaymentDto {

        Long bookingId;
        Double amount;
        UUID paymentId;
        String paymentUrl;
    }

