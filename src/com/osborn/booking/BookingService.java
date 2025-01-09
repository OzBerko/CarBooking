package com.osborn.booking;

import com.osborn.car.Car;
import com.osborn.user.User;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BookingService {
    private final BookingDao bookingDao;

    public BookingService(BookingDao bookingDao) {
        this.bookingDao = bookingDao;
    }

    public List<Bookings> getBookings() {
        return bookingDao.getBookings();
    }

    public Bookings bookCar(User user, ZonedDateTime startZonedDateTime, ZonedDateTime endZonedDateTime1, Car car) {
        return new Bookings(UUID.randomUUID(), user.getUserId(), startZonedDateTime, endZonedDateTime1,
                            car.getCarId()
        );
    }

    public Integer addBooking(Bookings booking) {
        return bookingDao.addBookings(booking);
    }

    public void save() {
        bookingDao.saveBookings();
    }

    public Optional<Bookings> deleteBooking(Integer bookingKey) {
        return bookingDao.deleteBooking(bookingKey);
    }
}
