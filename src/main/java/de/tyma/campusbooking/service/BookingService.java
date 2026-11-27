package de.tyma.campusbooking.service;


import de.tyma.campusbooking.dto.BookingView;
import de.tyma.campusbooking.model.*;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingService {

    private final Clock clock;
    private int ID_Counter = 1;
    private final List<Booking> Bookings = new ArrayList<>();

    public BookingService(Clock clock) {
        if(clock == null) {
            throw new IllegalArgumentException("Clock must not be null");
        }
        this.clock = clock;
    }

    public BookingView createBooking(User user, Room room, BookingPeriod period) {
        Booking booking = new Booking(ID_Counter, user, room, period);
        if(checkStartInPast(booking)) {
            throw new IllegalArgumentException("Booking must not start in the past");
        }
        if(checkBookingOverlap(booking)) {
            throw new IllegalStateException("Booking time period overlaps with existing bookings");
        }
        addBooking(booking);
        ID_Counter++;
        return toView(booking);
    }

    public void cancelBooking(int Booking_id, User user) {
        if(user == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        LocalDateTime now = LocalDateTime.now(clock);

        for(Booking booking : Bookings) {
            if(booking.getBooking_id() == Booking_id) {
                if(booking.getUser().getUser_id() != user.getUser_id()) {
                    throw new IllegalArgumentException("Booking does not belong to this User");
                }
                if(!now.isBefore(booking.getBookingperiod().getEndTime())) {
                    throw new IllegalStateException("Booking has already ended");
                }
                booking.cancel();
                return;
            }
        }
        throw new IllegalArgumentException("Booking not found");
    }

    public List<BookingView> getBookingsForUser(User user) {
        if(user == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        List<BookingView> UserBookings = new ArrayList<>();
        for(Booking ExistingBooking : Bookings) {
            if(ExistingBooking.getUser().getUser_id() == user.getUser_id()) {
                UserBookings.add(toView(ExistingBooking));
            }
        }

        return UserBookings;

    }

    private void addBooking(Booking booking) {
        if(booking == null) {
            throw new IllegalArgumentException("Booking must not be null");
        }
        Bookings.add(booking);
    }

    private boolean checkBookingOverlap(Booking booking) {
        if(booking == null) {
            throw new IllegalArgumentException("Booking must not be null");
        }

        for(Booking ExistingBooking : Bookings) {
            if(ExistingBooking.getRoom().getRoom_id() == booking.getRoom().getRoom_id()) {
                if(ExistingBooking.getStatus() == BookingStatus.ACTIVE) {
                     if(ExistingBooking.getBookingperiod().overlaps(booking.getBookingperiod())) {
                         return true;
                     }
                }
            }
        }
        return false;
    }

    private boolean checkStartInPast(Booking booking) {
        if(booking == null) {
            throw new IllegalArgumentException("Booking must not be null");
        }
        LocalDateTime now = LocalDateTime.now(clock);
        return (booking.getBookingperiod().getStartTime().isBefore(now));

    }

    private BookingView toView(Booking booking) {
        return new BookingView(booking.getBooking_id(),
                booking.getUser().getUser_id(),
                booking.getRoom().getRoom_id(),
                booking.getBookingperiod().getStartTime(),
                booking.getBookingperiod().getEndTime(),
                booking.getStatus());
    }


}
