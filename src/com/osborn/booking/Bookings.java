package com.osborn.booking;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Bookings {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    private UUID bookingId;
    private UUID userId;
    private ZonedDateTime dateTimeOfBooking;
    private ZonedDateTime bookingStartDateTime;
    private ZonedDateTime bookingEndDateTime;
    private UUID carId;

    public Bookings(UUID bookingId, UUID userId, ZonedDateTime dateTimeOfBooking, ZonedDateTime bookingStartDateTime,
                    ZonedDateTime bookingEndDateTime, UUID carId) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.dateTimeOfBooking = dateTimeOfBooking;
        this.bookingStartDateTime = bookingStartDateTime;
        this.bookingEndDateTime = bookingEndDateTime;
        this.carId = carId;
    }

    public Bookings(UUID bookingId, UUID userId, ZonedDateTime bookingStartDateTime,
                    ZonedDateTime bookingEndDateTime, UUID carId) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.dateTimeOfBooking = ZonedDateTime.now();
        this.bookingStartDateTime = bookingStartDateTime;
        this.bookingEndDateTime = bookingEndDateTime;
        this.carId = carId;
    }

    public static Optional<UUID> validateUuidFormat(String bookingIdStr) {
        try {
            return Optional.of(UUID.fromString(bookingIdStr));
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format: " + bookingIdStr);
            return Optional.empty();
        }
    }

    public static Optional<ZonedDateTime> validateDateTimeFormat(String dateTime){
        try {
            return Optional.of(ZonedDateTime.parse(dateTime));
        } catch (DateTimeParseException e){
            System.out.println("Invalid ZonedDateTime format: " + dateTime);
            return Optional.empty();
        }
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public ZonedDateTime getDateTimeOfBooking() {
        return dateTimeOfBooking;
    }

    public void setDateTimeOfBooking(ZonedDateTime dateTimeOfBooking) {
        this.dateTimeOfBooking = dateTimeOfBooking;
    }

    public ZonedDateTime getBookingStartDateTime() {
        return bookingStartDateTime;
    }

    public void setBookingStartDateTime(ZonedDateTime bookingStartDateTime) {
        this.bookingStartDateTime = bookingStartDateTime;
    }

    public ZonedDateTime getBookingEndDateTime() {
        return bookingEndDateTime;
    }

    public void setBookingEndDateTime(ZonedDateTime bookingEndDateTime) {
        this.bookingEndDateTime = bookingEndDateTime;
    }

    public UUID getCarId() {
        return carId;
    }

    public void setCarId(UUID carId) {
        this.carId = carId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bookings bookings = (Bookings) o;
        return Objects.equals(bookingId, bookings.bookingId) && Objects.equals(userId,
                                                                               bookings.userId) && Objects.equals(
                dateTimeOfBooking, bookings.dateTimeOfBooking) && Objects.equals(bookingStartDateTime,
                                                                                 bookings.bookingStartDateTime) && Objects.equals(
                bookingEndDateTime, bookings.bookingEndDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, userId, dateTimeOfBooking, bookingStartDateTime, bookingEndDateTime);
    }

    @Override
    public String toString() {
        return "Bookings{" +
               "bookingId=" + bookingId +
               ", userId=" + userId +
               ", dateTimeOfBooking=" + dateTimeOfBooking +
               ", bookingStartDateTime=" + bookingStartDateTime +
               ", bookingEndDateTime=" + bookingEndDateTime +
               ", carId=" + carId +
               '}';
    }
}
