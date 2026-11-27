package de.tyma.campusbooking.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookingPeriodTest {

    private static  final LocalDateTime StartTime = LocalDateTime.of(2026, 12, 1, 10, 0);
    private static final LocalDateTime EndTime = LocalDateTime.of(2026, 12, 1, 12, 0);

    @Test
    void acceptValidBookingPeriod() {
        BookingPeriod period = new BookingPeriod(StartTime, EndTime);

        assertEquals(StartTime, period.getStartTime());
        assertEquals(EndTime, period.getEndTime());
    }

    @Test
    void rejectZeroBookingPeriod() {
        assertThrows(IllegalArgumentException.class, () -> {
            new BookingPeriod(StartTime, StartTime);
        });
    }

    @Test
    void rejectNegativeBookingPeriod() {
        assertThrows(IllegalArgumentException.class, () -> {
            new BookingPeriod(StartTime.plusHours(3), EndTime);
        });
    }

    @Test
    void rejectNullStartTimeBookingPeriod() {
        assertThrows(IllegalArgumentException.class, () -> {
            new BookingPeriod(null, EndTime);
        });
    }

    @Test
    void rejectNullEndTimeBookingPeriod() {
        assertThrows(IllegalArgumentException.class, () -> {
            new BookingPeriod(StartTime, null);
        });
    }

    @Test
    void rejectTooBigBookingPeriod() {
        assertThrows(IllegalArgumentException.class, () -> {
            new BookingPeriod(StartTime, StartTime.plusHours(6).plusSeconds(1));
        });
    }





}
