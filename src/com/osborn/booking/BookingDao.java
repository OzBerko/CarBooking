package com.osborn.booking;

import java.util.List;
import java.util.Optional;

public interface BookingDao {
    public List<Bookings> getBookings();

    public Integer addBookings(Bookings booking);

    public void saveBookings();

    Optional<Bookings> deleteBooking(Integer bookingKey);
}
