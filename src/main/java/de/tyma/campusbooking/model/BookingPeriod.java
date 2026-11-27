package de.tyma.campusbooking.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class BookingPeriod {

    private static final Duration MAX_BOOKING_TIME = Duration.ofHours(6);

    private final LocalDateTime StartTime;
    private final LocalDateTime EndTime;

    public BookingPeriod(LocalDateTime StartTime, LocalDateTime EndTime) throws IllegalArgumentException {
        if(StartTime == null || EndTime == null || !EndTime.isAfter(StartTime) || Duration.between(StartTime, EndTime).compareTo(MAX_BOOKING_TIME) > 0) {
            throw new IllegalArgumentException("Invalid Time Period");
        }
        this.StartTime = StartTime;
        this.EndTime = EndTime;

    }

    public LocalDateTime getStartTime() {
        return StartTime;
    }

    public LocalDateTime getEndTime() {
        return EndTime;
    }

    public boolean overlaps(BookingPeriod Other) {
        if(Other == null) {
            throw new IllegalArgumentException("Other period must not be null");
        }
        return (this.getStartTime().isBefore(Other.getEndTime()) && this.getEndTime().isAfter(Other.getStartTime()));
    }

}
