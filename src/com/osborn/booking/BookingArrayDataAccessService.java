package com.osborn.booking;

import com.osborn.car.Car;
import com.osborn.user.User;

import java.io.*;
import java.time.ZonedDateTime;
import java.util.*;

public class BookingArrayDataAccessService implements BookingDao {
    private final static File file = new File("src/com/osborn/booking/bookings.csv");

    private final static Map<Integer, Bookings> bookings = new HashMap<>();
    private static int bookingIndex = 0;

    static {
        loadBookingsFromFile();
    }

    private static void loadBookingsFromFile() {
        if (!file.exists() || !file.isFile()) {
            System.err.println("Bookings file does not exist or is invalid.");
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            if (!scanner.hasNextLine()) {
                System.err.println("Empty bookings file: No valid bookings found in the file.");
            }
            while (scanner.hasNextLine()) {
                String inputBooking = scanner.nextLine();

                createBooking(inputBooking).ifPresentOrElse(booking -> {
                                                                bookings.put(bookingIndex++, booking);
                                                                System.out.println("Booking added: " + booking);
                                                            },
                                                            () -> System.out.println(
                                                                    "Invalid car data, skipping: " + inputBooking));
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error occurred while loading bookings from file: " + e.getMessage());
        }
    }

    private static Optional<Bookings> createBooking(String inputBooking) {
        String[] bookingFeats = inputBooking.split(",");
        if (bookingFeats.length < 6) {
            return Optional.empty();
        }

        String bookingIdStr = bookingFeats[0].trim();
        Optional<UUID> bookingUuid = Bookings.validateUuidFormat(bookingIdStr);

        String userIdStr = bookingFeats[1].trim();
        Optional<UUID> userUuid = User.validateUuid(userIdStr);

        String carIdStr = bookingFeats[5].trim();
        Optional<UUID> carUuid = Car.validateUuidFormat(carIdStr);

        String dateTimeOfBookingStr = bookingFeats[2].trim();
        Optional<ZonedDateTime> dateTimeOfBookingOpt = Bookings.validateDateTimeFormat(dateTimeOfBookingStr);

        String bookingStartDateTimeStr = bookingFeats[3].trim();
        Optional<ZonedDateTime> bookingStartDateTimeOpt = Bookings.validateDateTimeFormat(bookingStartDateTimeStr);

        String bookingEndDateTimeStr = bookingFeats[4].trim();
        Optional<ZonedDateTime> bookingEndDateTimeStrOpt = Bookings.validateDateTimeFormat(bookingEndDateTimeStr);

        if (bookingUuid.isPresent() &&
            userUuid.isPresent() &&
            carUuid.isPresent() &&
            dateTimeOfBookingOpt.isPresent() &&
            bookingStartDateTimeOpt.isPresent() &&
            bookingEndDateTimeStrOpt.isPresent()) {
            return Optional.of(new Bookings(bookingUuid.get(),
                                            userUuid.get(),
                                            dateTimeOfBookingOpt.get(),
                                            bookingStartDateTimeOpt.get(),
                                            bookingEndDateTimeStrOpt.get(),
                                            carUuid.get()));
        } else {
            System.out.println("Failed to create booking due to invalid UUID");
        }
        return Optional.empty();
    }


    @Override
    public List<Bookings> getBookings() {
        return new ArrayList<>(bookings.values());
    }

    @Override
    public Integer addBookings(Bookings booking) {
        bookings.put(bookingIndex, booking);
        System.out.println("A new booking has been added");
        Integer key = bookingIndex;
        bookingIndex++;
        return key;
    }

    @Override
    public void saveBookings() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (Bookings booking : bookings.values()) {
                writer.write(booking.getBookingId() + ", "
                             + booking.getUserId() + ", "
                             + booking.getDateTimeOfBooking() + ", "
                             + booking.getBookingStartDateTime() + ", "
                             + booking.getBookingEndDateTime() + ", "
                             + booking.getCarId());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    @Override
    public Optional<Bookings> deleteBooking(Integer bookingKey) {
        return Optional.ofNullable(bookings.remove(bookingKey));
    }
}