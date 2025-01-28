package com.osborn.booking;

import java.util.List;
import java.util.Optional;

public interface BookingDao {
    List<Bookings> getBookings();

    Integer addBookings(Bookings booking);

    void saveBookings();

    Optional<Bookings> deleteBooking(Integer bookingKey);
}
