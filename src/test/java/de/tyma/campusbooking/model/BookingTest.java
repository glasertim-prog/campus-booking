package de.tyma.campusbooking.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {

    private static  final LocalDateTime StartTime = LocalDateTime.of(2026, 12, 1, 10, 0);
    private static final LocalDateTime EndTime = LocalDateTime.of(2026, 12, 1, 12, 0);

    private User createUser() {
        return new User("Tyma", "Tyma@example.com", 1);
    }

    private Room createTestRoom() {
        return new Room("B-501", 1, 25);
    }

    private BookingPeriod createBookingPeriod() {
        return new BookingPeriod(StartTime, EndTime);
    }

    private Booking createBooking() {
        User user = createUser();
        Room room = createTestRoom();
        BookingPeriod period = createBookingPeriod();
        return new Booking(1, user, room, period);
    }

    @Test
    void acceptValidBooking() {
        User user = createUser();
        Room room = createTestRoom();
        BookingPeriod period = createBookingPeriod();

        Booking booking = new Booking(1, user, room, period);

        assertEquals(1, booking.getBooking_id());
        assertSame(user, booking.getUser());
        assertSame(room, booking.getRoom());
        assertSame(period, booking.getBookingperiod());
        assertEquals(BookingStatus.ACTIVE, booking.getStatus());
    }

    @Test
    void correctCancellation() {
        Booking booking = createBooking();
        booking.cancel();
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
    }

    @Test
    void rejectCancelledCancellation() {
        Booking booking = createBooking();
        booking.cancel();

        assertThrows(IllegalStateException.class, () -> {
            booking.cancel();
        });

    }

    @Test
    void rejectCancelledCancellationKeepCancelled() {
        Booking booking = createBooking();
        booking.cancel();

        assertThrows(IllegalStateException.class, () -> {
            booking.cancel();
        });

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
    }

    @Test
    void rejectNullUser() {
        Room room = createTestRoom();
        BookingPeriod period = createBookingPeriod();

        assertThrows(IllegalArgumentException.class, () -> {
            new Booking(1, null, room, period);
        });
    }

    @Test
    void rejectNullRoom() {
        User user = createUser();
        BookingPeriod period = createBookingPeriod();

        assertThrows(IllegalArgumentException.class, () -> {
            new Booking(1, user, null, period);
        });
    }

    @Test
    void rejectNullperiod() {
        User user = createUser();
        Room room = createTestRoom();

        assertThrows(IllegalArgumentException.class, () -> {
            new Booking(1, user, room, null);
        });
    }
}
