package org.example.dto;

public record NotificationMessage(Long bookingId, Long hostId, String title, String description) {}
