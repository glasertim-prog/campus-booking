# Cancel a booking

## Goal
User cancels a booking.

## Preconditions
1. User has to be the one that booked the room.
2. The Booking has not ended yet.

## Main flow
1. System displays Users bookings.
2. User selects the booking.
3. User confirms to cancel the booking.
4. System verifies booking belongs to User.
5. System checks the status being `ACTIVE`.
6. System sets booking status to `CANCELLED`.

## Result 
The canceled booking remains visible but no longer blocks availability. For an ongoing booking, the remaining time becomes available immediately.
