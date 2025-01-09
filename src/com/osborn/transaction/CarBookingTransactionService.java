package com.osborn.transaction;

import com.osborn.booking.BookingService;
import com.osborn.booking.Bookings;
import com.osborn.car.Car;
import com.osborn.car.CarService;
import com.osborn.user.User;
import com.osborn.user.UserService;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

public class CarBookingTransactionService {
    private final UserService userService;
    private final CarService carService;
    private final BookingService bookingService;

    public CarBookingTransactionService(UserService userService, CarService carService, BookingService bookingService) {
        this.userService = userService;
        this.carService = carService;
        this.bookingService = bookingService;
    }

    public void bookCar(String firstName, String lastName, ZonedDateTime startZonedDateTime,
                        ZonedDateTime endZonedDateTime1, UUID carId) {
        boolean transactionSuccessful = false;
        Integer userKey = -1;
        Integer bookingKey = -1;
        User user = userService.createUser(firstName, lastName);
        Optional<Car> carOpt = carService.getCar(carId);
        if (carOpt.isPresent()) {
            Bookings booking = bookingService.bookCar(user, startZonedDateTime, endZonedDateTime1, carOpt.get());

            try {
                userKey = userService.addUser(user);
                carService.setCarToBooked(carOpt.get(), user);
                bookingKey = bookingService.addBooking(booking);
                transactionSuccessful = true;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                if (transactionSuccessful) {
                    userService.save();
                    carService.save();
                    bookingService.save();

                    System.out.println("Booking " + booking.getBookingId() + "complete.");
                } else {
                    Optional<User> deletedUserOpt = userService.deleteUser(userKey);
                    carService.setCarToNotBooked(carOpt.get());
                    Optional<Bookings> deletedBookingOpt = bookingService.deleteBooking(bookingKey);

                    if (deletedUserOpt.isPresent() && deletedBookingOpt.isPresent()) {
                        System.out.println("The following entries were deleted: " +
                                           "\nUser database: " + deletedUserOpt.get() +
                                           "\nBooking database: " + deletedBookingOpt.get());
                    } else if (deletedUserOpt.isEmpty() && deletedBookingOpt.isEmpty()) {
                        System.out.println("Booking was unsuccesful. No entries were deleted from the database.");
                    } else if (deletedUserOpt.isEmpty()) {
                        System.out.println(
                                "Booking was unsuccesful. The booking " + deletedBookingOpt.get() + "was deleted from the database");
                    } else {
                        System.out.println(
                                "Booking was unsuccesful. The booking " + deletedUserOpt.get() + "was deleted from the database");
                    }
                }
            }
        }
    }
}
