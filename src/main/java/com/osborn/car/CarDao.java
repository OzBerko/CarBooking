package com.osborn.car;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarDao {

    Optional<Car> getCar(UUID carId);
    List<Car> getCars();
    Integer addCar(Car car);
    void saveCars();
}
