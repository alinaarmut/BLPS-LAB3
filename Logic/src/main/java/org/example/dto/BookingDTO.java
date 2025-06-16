package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.primary.Booking;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private String title;
    private String description;
    private Double pricePerNight;
    private Long hostId;
    private Long adId;

    public static BookingDTO fromEntity(Booking booking) {
        return new BookingDTO(
                booking.getTitle(),
                booking.getDescription(),
                booking.getPricePerNight(),
                booking.getHostId(),
                booking.getAd().getId()
        );
    }
}
