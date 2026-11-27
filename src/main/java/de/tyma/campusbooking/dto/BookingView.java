package de.tyma.campusbooking.dto;

import de.tyma.campusbooking.model.BookingStatus;

import java.time.LocalDateTime;

public record BookingView(
        int BookingId,
        int UserId,
        int RoomId,
        LocalDateTime StartTime,
        LocalDateTime EndTime,
        BookingStatus Status
) {}
