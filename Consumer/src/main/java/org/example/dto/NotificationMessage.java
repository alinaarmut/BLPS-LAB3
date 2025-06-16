package org.example.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class NotificationMessage implements Serializable {

    private Long bookingId;
    private Long hostId;
    private String title;
    private String description;
}