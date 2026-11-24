## Goals

- Consistent system for booking rooms in a simple and intuitive way while preventing conflicting bookings.

## First version

- Users can book a room by choosing a start and end time.
- Bookings for the same room must not overlap in time.
- Duration of a booking cant exceed 6 hours.
- Users can cancel their booking before or during the booking duration.

## Booking rules

- The end time must be after the start time.
- Bookings cannot start in the past.
- A booking may start exactly when another booking ends.
- Only the user who created a booking can cancel it.
- Canceling a booking immediately releases the room for the remaining time.
- Cancelled bookings remain visible in the user's booking history and no longer block room availability.