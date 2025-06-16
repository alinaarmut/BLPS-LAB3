package org.example.entity.primary;


import jakarta.persistence.*;
import lombok.*;
import org.example.entity.enums_status.BookingStatus;
import org.example.entity.enums_status.OfferStatus;


import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "reservation")
@NoArgsConstructor

@Getter
@Setter

//бронирование
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Double pricePerNight;


    private Long hostId;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
    private LocalDateTime timestamp;


    @Enumerated(EnumType.STRING)
    private OfferStatus offerStatus;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "ad_id")
    private Ad ad;


    @Version
    private Long version;
}
