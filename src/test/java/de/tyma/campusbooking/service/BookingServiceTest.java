package de.tyma.campusbooking.service;

import de.tyma.campusbooking.dto.BookingView;
import de.tyma.campusbooking.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class BookingServiceTest {

    private static final ZoneId ZONE = ZoneId.of("Europe/Berlin");
    private static final LocalDateTime NOW =
            LocalDateTime.of(2026, 12, 1, 10, 0);

    private MutableClock clock;
    private BookingService service;
    private User user;
    private User otherUser;
    private Room room;

    @BeforeEach
    void setUp() {
        clock = new MutableClock(NOW, ZONE);
        service = new BookingService(clock);

        user = new User("Tyma", "tyma@example.com", 1);
        otherUser = new User("Alex", "alex@example.com", 2);
        room = new Room("B-501", 1, 25);
    }

    private BookingPeriod period(int StartHour, int EndHour) {
        return new BookingPeriod(
                NOW.withHour(StartHour),
                NOW.withHour(EndHour)
        );
    }

    private BookingView createBooking() {
        return service.createBooking(user, room, period(11, 13));
    }

    private BookingView findBooking(User owner, int BookingId) {
        return service.getBookingsForUser(owner).stream()
                .filter(view -> view.BookingId() == BookingId)
                .findFirst()
                .orElseThrow();
    }

    @Test
    void rejectsNullClock() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new BookingService(null)
        );
    }

    @Test
    void createsAndStoresActiveBooking() {
        BookingPeriod requestedPeriod = period(11, 13);

        BookingView booking =
                service.createBooking(user, room, requestedPeriod);

        assertEquals(user.getUser_id(), booking.UserId());
        assertEquals(room.getRoom_id(), booking.RoomId());
        assertEquals(requestedPeriod.getStartTime(), booking.StartTime());
        assertEquals(requestedPeriod.getEndTime(), booking.EndTime());
        assertEquals(BookingStatus.ACTIVE, booking.Status());

        assertEquals(List.of(booking), service.getBookingsForUser(user));
    }

    @Test
    void assignsDifferentIdsToSuccessfulBookings() {
        BookingView first =
                service.createBooking(user, room, period(11, 12));
        BookingView second =
                service.createBooking(user, room, period(12, 13));

        assertNotEquals(first.BookingId(), second.BookingId());
        assertEquals(2, service.getBookingsForUser(user).size());
    }

    @Test
    void rejectsNullUserWithoutStoringBooking() {
        assertThrows(IllegalArgumentException.class, () ->
                service.createBooking(null, room, period(11, 13)));

        assertTrue(service.getBookingsForUser(user).isEmpty());
    }

    @Test
    void rejectsNullRoomWithoutStoringBooking() {
        assertThrows(IllegalArgumentException.class, () ->
                service.createBooking(user, null, period(11, 13)));

        assertTrue(service.getBookingsForUser(user).isEmpty());
    }

    @Test
    void rejectsNullPeriodWithoutStoringBooking() {
        assertThrows(IllegalArgumentException.class, () ->
                service.createBooking(user, room, null));

        assertTrue(service.getBookingsForUser(user).isEmpty());
    }

    @Test
    void rejectsStartInPastWithoutStoringBooking() {
        BookingPeriod pastPeriod =
                new BookingPeriod(NOW.minusSeconds(1), NOW.plusHours(1));

        assertThrows(IllegalArgumentException.class, () ->
                service.createBooking(user, room, pastPeriod));

        assertTrue(service.getBookingsForUser(user).isEmpty());
    }

    @Test
    void acceptsStartExactlyAtCurrentTime() {
        BookingPeriod requestedPeriod =
                new BookingPeriod(NOW, NOW.plusHours(1));

        BookingView booking =
                service.createBooking(user, room, requestedPeriod);

        assertEquals(NOW, booking.StartTime());
        assertEquals(BookingStatus.ACTIVE, booking.Status());
    }

    @Test
    void rejectsOverlapEvenForDifferentUser() {
        BookingView existing = createBooking();

        assertThrows(IllegalStateException.class, () ->
                service.createBooking(otherUser, room, period(12, 14)));

        assertEquals(List.of(existing), service.getBookingsForUser(user));
        assertTrue(service.getBookingsForUser(otherUser).isEmpty());
    }

    @Test
    void identifiesSameRoomByIdInsteadOfObjectIdentity() {
        createBooking();

        Room sameRoomId = new Room("B-501", room.getRoom_id(), 25);

        assertThrows(IllegalStateException.class, () ->
                service.createBooking(user, sameRoomId, period(12, 14)));
    }

    @Test
    void allowsOverlapForDifferentRoomsWithSameRoomNumber() {
        createBooking();

        Room differentRoom = new Room("B-501", 2, 25);

        BookingView booking =
                service.createBooking(user, differentRoom, period(11, 13));

        assertEquals(2, booking.RoomId());
        assertEquals(2, service.getBookingsForUser(user).size());
    }

    @Test
    void allowsAdjacentBookingsBeforeAndAfterExistingBooking() {
        createBooking(); // 11:00–13:00

        service.createBooking(user, room, period(10, 11));
        service.createBooking(user, room, period(13, 14));

        assertEquals(3, service.getBookingsForUser(user).size());
    }

    @Test
    void laterNonOverlappingBookingDoesNotHideEarlierConflict() {
        createBooking(); // 11:00–13:00
        service.createBooking(user, room, period(15, 16));

        assertThrows(IllegalStateException.class, () ->
                service.createBooking(user, room, period(12, 14)));

        assertEquals(2, service.getBookingsForUser(user).size());
    }

    @Test
    void allowsRebookingCancelledPeriod() {
        BookingView original = createBooking();
        service.cancelBooking(original.BookingId(), user);

        BookingView replacement = createBooking();

        assertNotEquals(original.BookingId(), replacement.BookingId());
        assertEquals(BookingStatus.ACTIVE, replacement.Status());
        assertEquals(
                BookingStatus.CANCELLED,
                findBooking(user, original.BookingId()).Status()
        );
        assertEquals(2, service.getBookingsForUser(user).size());
    }

    @Test
    void cancelledBookingDoesNotHideAnotherActiveConflict() {
        BookingView cancelled =
                service.createBooking(user, room, period(11, 12));
        service.cancelBooking(cancelled.BookingId(), user);

        service.createBooking(user, room, period(12, 14));

        assertThrows(IllegalStateException.class, () ->
                service.createBooking(user, room, period(11, 13)));
    }

    @Test
    void cancelsFutureBookingAndKeepsItVisible() {
        BookingView booking = createBooking();

        service.cancelBooking(booking.BookingId(), user);

        assertEquals(1, service.getBookingsForUser(user).size());
        assertEquals(
                BookingStatus.CANCELLED,
                findBooking(user, booking.BookingId()).Status()
        );
    }

    @Test
    void cancelsOngoingBookingAndReleasesRemainingTime() {
        BookingView booking = createBooking(); // 11:00–13:00
        clock.setTime(NOW.withHour(12));

        service.cancelBooking(booking.BookingId(), user);

        BookingView replacement =
                service.createBooking(otherUser, room, period(12, 13));

        assertEquals(BookingStatus.ACTIVE, replacement.Status());
        assertEquals(
                BookingStatus.CANCELLED,
                findBooking(user, booking.BookingId()).Status()
        );
    }

    @Test
    void rejectsCancellationExactlyAtEndTime() {
        BookingView booking = createBooking();
        clock.setTime(booking.EndTime());

        assertThrows(IllegalStateException.class, () ->
                service.cancelBooking(booking.BookingId(), user));

        assertEquals(
                BookingStatus.ACTIVE,
                findBooking(user, booking.BookingId()).Status()
        );
    }

    @Test
    void rejectsCancellationAfterEndTime() {
        BookingView booking = createBooking();
        clock.setTime(booking.EndTime().plusSeconds(1));

        assertThrows(IllegalStateException.class, () ->
                service.cancelBooking(booking.BookingId(), user));

        assertEquals(
                BookingStatus.ACTIVE,
                findBooking(user, booking.BookingId()).Status()
        );
    }

    @Test
    void rejectsCancellationByDifferentUser() {
        BookingView booking = createBooking();

        assertThrows(IllegalArgumentException.class, () ->
                service.cancelBooking(booking.BookingId(), otherUser));

        assertEquals(
                BookingStatus.ACTIVE,
                findBooking(user, booking.BookingId()).Status()
        );
    }

    @Test
    void identifiesBookingOwnerByIdInsteadOfObjectIdentity() {
        BookingView booking = createBooking();
        User sameUserId = new User("Tyma", "tyma@example.com", 1);

        service.cancelBooking(booking.BookingId(), sameUserId);

        assertEquals(
                BookingStatus.CANCELLED,
                findBooking(user, booking.BookingId()).Status()
        );
    }

    @Test
    void rejectsCancellationWithNullUser() {
        BookingView booking = createBooking();

        assertThrows(IllegalArgumentException.class, () ->
                service.cancelBooking(booking.BookingId(), null));

        assertEquals(
                BookingStatus.ACTIVE,
                findBooking(user, booking.BookingId()).Status()
        );
    }

    @Test
    void rejectsCancellationOfUnknownBooking() {
        assertThrows(IllegalArgumentException.class, () ->
                service.cancelBooking(999, user));
    }

    @Test
    void rejectsRepeatedCancellationAndKeepsCancelledStatus() {
        BookingView booking = createBooking();
        service.cancelBooking(booking.BookingId(), user);

        assertThrows(IllegalStateException.class, () ->
                service.cancelBooking(booking.BookingId(), user));

        assertEquals(
                BookingStatus.CANCELLED,
                findBooking(user, booking.BookingId()).Status()
        );
    }

    @Test
    void cancellationDoesNotChangeOtherBookings() {
        BookingView first =
                service.createBooking(user, room, period(11, 12));
        BookingView second =
                service.createBooking(user, room, period(12, 13));

        service.cancelBooking(second.BookingId(), user);

        assertEquals(
                BookingStatus.ACTIVE,
                findBooking(user, first.BookingId()).Status()
        );
        assertEquals(
                BookingStatus.CANCELLED,
                findBooking(user, second.BookingId()).Status()
        );
    }

    @Test
    void returnsOnlyBookingsOfRequestedUser() {
        BookingView ownBooking = createBooking();
        service.createBooking(otherUser, room, period(13, 14));

        User sameUserId = new User("Tyma", "tyma@example.com", 1);

        assertEquals(
                List.of(ownBooking),
                service.getBookingsForUser(sameUserId)
        );
    }

    @Test
    void returnsEmptyListForUserWithoutBookings() {
        createBooking();

        assertTrue(service.getBookingsForUser(otherUser).isEmpty());
    }

    @Test
    void rejectsNullUserWhenListingBookings() {
        assertThrows(IllegalArgumentException.class, () ->
                service.getBookingsForUser(null));
    }

    @Test
    void includesPastAndCancelledBookings() {
        BookingView pastBooking =
                service.createBooking(user, room, period(11, 12));
        BookingView cancelledBooking =
                service.createBooking(user, room, period(13, 14));

        service.cancelBooking(cancelledBooking.BookingId(), user);
        clock.setTime(NOW.withHour(15));

        List<BookingView> result = service.getBookingsForUser(user);

        assertEquals(2, result.size());
        assertTrue(result.stream()
                .anyMatch(view -> view.BookingId() == pastBooking.BookingId()));
        assertEquals(
                BookingStatus.CANCELLED,
                findBooking(user, cancelledBooking.BookingId()).Status()
        );
    }

    @Test
    void modifyingReturnedListDoesNotChangeStoredBookings() {
        BookingView booking = createBooking();

        List<BookingView> result = service.getBookingsForUser(user);
        result.clear();

        assertEquals(List.of(booking), service.getBookingsForUser(user));
    }

    @Test
    void previouslyReturnedViewsRemainSnapshotsAfterCancellation() {
        BookingView creationView = createBooking();
        BookingView listedView =
                findBooking(user, creationView.BookingId());

        service.cancelBooking(creationView.BookingId(), user);

        assertEquals(BookingStatus.ACTIVE, creationView.Status());
        assertEquals(BookingStatus.ACTIVE, listedView.Status());
        assertEquals(
                BookingStatus.CANCELLED,
                findBooking(user, creationView.BookingId()).Status()
        );
    }

    private static final class MutableClock extends Clock {

        private final AtomicReference<Instant> currentInstant;
        private final ZoneId zone;

        private MutableClock(LocalDateTime initialTime, ZoneId zone) {
            this(
                    new AtomicReference<>(
                            initialTime.atZone(zone).toInstant()),
                    zone
            );
        }

        private MutableClock(
                AtomicReference<Instant> currentInstant,
                ZoneId zone
        ) {
            this.currentInstant = currentInstant;
            this.zone = zone;
        }

        private void setTime(LocalDateTime time) {
            currentInstant.set(time.atZone(zone).toInstant());
        }

        @Override
        public ZoneId getZone() {
            return zone;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return new MutableClock(currentInstant, zone);
        }

        @Override
        public Instant instant() {
            return currentInstant.get();
        }
    }
}