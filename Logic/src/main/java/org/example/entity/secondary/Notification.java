package org.example.entity.secondary;


import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "notification")

public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "booking_id", nullable = false)
    private Long bookingId;

public Notification(Long userId, Long bookingId) {
    this.userId = userId;
    this.bookingId = bookingId;
}

    public Notification() {

    }
}
