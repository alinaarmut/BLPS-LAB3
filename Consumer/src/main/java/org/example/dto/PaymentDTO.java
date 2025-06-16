package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    Long bookingId;
    UUID paymentId;
    Double amount;
    Boolean success;
}
