package org.example.entity.primary;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.entity.enums_status.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

//платеж
@Entity
@Getter
@Setter
public class Payment {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private Long bookingId;
//    private Double amount;
//
//    @Enumerated(EnumType.STRING)
//    private PaymentStatus status;
//
//    private LocalDateTime paidAt;
//    private String externalPaymentId;
//}
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

    private Long bookingId;
    private Long offerId;

    @Column(unique = true)
    private UUID paymentId;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime createdAt;
    public boolean isSuccess() {
        return status == PaymentStatus.SUCCESS;
    }
}
