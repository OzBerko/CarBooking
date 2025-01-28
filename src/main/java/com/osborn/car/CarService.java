package com.osborn.car;

import com.osborn.user.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CarService {
    private final CarDao carDao;

    public CarService(CarDao carDao) {
        this.carDao = carDao;
    }

    public List<Car> getCars() {
        return carDao.getCars();
    }

    public Optional<Car> getCar(UUID carIdInput) {
        return carDao.getCar(carIdInput);
    }

    public boolean availableCars() {
        Car[] cars = this.carsAvailable();
        return cars.length > 0;
    }

    public Car[] carsAvailable() {
        return carDao.getCars()
                     .stream()
                     .filter(car -> car != null && !car.isBooked())
                     .toArray(Car[]::new);
    }

    public void setCarToBooked(Car car, User user) {
        car.setBooked(true);
        car.setUserId(user.getUserId());
    }

    public void save() {
        carDao.saveCars();
    }

    public void setCarToNotBooked(Car car) {
        car.setBooked(false);
        car.setUserId(null);
    }
}
