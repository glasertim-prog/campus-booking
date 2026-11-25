# Book a room

## Goal
The User books a certain room for a selected time period.

## Preconditions
1. Room is not booked for the chosen time.
2. User has not already booked this room on the current day.


## Main flow 
1. User selects a room.
2. System only shows available time periods.
3. User selects time period for.
4. User confirms selection.
5. System checks start and end time (max 6h and start time must lie before end time).
6. System checks for any booking conflicts.
7. System saves booking.
8. System displays booking confirmation

## Errors
If system finds a booking conflict (Users confirming at the same time), only one conflicting booking can be safed successfully. Other requests get rejected by an error message.

## Result
The chosen room is now booked for the chosen time period and cant be booked for this period by other Users.

