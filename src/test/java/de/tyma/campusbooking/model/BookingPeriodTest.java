package de.tyma.campusbooking.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

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

    @Test
    void detectsPartialOverlap() {
        BookingPeriod first =
                new BookingPeriod(StartTime, StartTime.plusHours(2));
        BookingPeriod second =
                new BookingPeriod(StartTime.plusHours(1), StartTime.plusHours(3));

        assertTrue(first.overlaps(second));
        assertTrue(second.overlaps(first));
    }

    @Test
    void detectsIdenticalPeriods() {
        BookingPeriod first =
                new BookingPeriod(StartTime, StartTime.plusHours(2));
        BookingPeriod second =
                new BookingPeriod(StartTime, StartTime.plusHours(2));

        assertTrue(first.overlaps(second));
    }

    @Test
    void detectsContainedPeriod() {
        BookingPeriod outer =
                new BookingPeriod(StartTime, StartTime.plusHours(4));
        BookingPeriod inner =
                new BookingPeriod(StartTime.plusHours(1), StartTime.plusHours(2));

        assertTrue(outer.overlaps(inner));
        assertTrue(inner.overlaps(outer));
    }

    @Test
    void allowsAdjacentPeriods() {
        BookingPeriod first =
                new BookingPeriod(StartTime, StartTime.plusHours(2));
        BookingPeriod second =
                new BookingPeriod(StartTime.plusHours(2), StartTime.plusHours(4));

        assertFalse(first.overlaps(second));
        assertFalse(second.overlaps(first));
    }

    @Test
    void detectsSeparatedPeriods() {
        BookingPeriod first =
                new BookingPeriod(StartTime, StartTime.plusHours(2));
        BookingPeriod second =
                new BookingPeriod(StartTime.plusHours(3), StartTime.plusHours(5));

        assertFalse(first.overlaps(second));
        assertFalse(second.overlaps(first));
    }

    @Test
    void rejectsNullPeriodWhenCheckingOverlap() {
        BookingPeriod period =
                new BookingPeriod(StartTime, StartTime.plusHours(2));

        assertThrows(IllegalArgumentException.class, () -> period.overlaps(null));
    }





}
