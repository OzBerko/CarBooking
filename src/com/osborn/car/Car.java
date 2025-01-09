package com.osborn.car;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Car {
    private UUID carId;
    private UUID userId;
    private boolean booked;

    public Car(UUID carId, UUID userId, boolean booked) {
        this.carId = carId;
        this.userId = userId;
        this.booked = booked;
    }

    public Car(UUID carId) {
        this.carId = carId;
        this.userId = null;
        this.booked = false;
    }

    public static Optional<UUID> validateUuidFormat(String carIdStr) {
        try {
            return Optional.of(UUID.fromString(carIdStr));
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Car UUID format: " + carIdStr);
            return Optional.empty();
        }
    }

    public UUID getCarId() {
        return carId;
    }

    public void setCarId(UUID carId) {
        this.carId = carId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return booked == car.booked && Objects.equals(carId, car.carId) && Objects.equals(userId, car.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carId, userId, booked);
    }

    @Override
    public String toString() {
        return "Car{" +
               "carId=" + carId +
               ", userId=" + userId +
               ", booked=" + booked +
               '}';
    }
}
