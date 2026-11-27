package de.tyma.campusbooking.model;

public class Booking {

    private final int Booking_id;
    private final User user;
    private final Room room;
    private final BookingPeriod period;
    private BookingStatus Status;

    public Booking(int Booking_id, User user, Room room, BookingPeriod period) {
        if(user == null) {
            throw new IllegalArgumentException("User must not be null");
        }
        if(room == null) {
            throw new IllegalArgumentException("Room must not be null");
        }
        if(period == null) {
            throw new IllegalArgumentException("Period must not be null");
        }
        this.Booking_id = Booking_id;
        this.user = user;
        this.room = room;
        this.period = period;
        this.Status = BookingStatus.ACTIVE;

    }

    public int getBooking_id() {
        return Booking_id;
    }

    public User getUser() {
        return user;
    }

    public Room getRoom() {
        return room;
    }

    public BookingPeriod getBookingperiod() {
        return period;
    }

    public BookingStatus getStatus() {
        return Status;
    }

    public void cancel() throws IllegalStateException{
        if(Status != BookingStatus.ACTIVE) {
            throw new IllegalStateException("Booking is not active");
        }
        Status = BookingStatus.CANCELLED;
    }
}
