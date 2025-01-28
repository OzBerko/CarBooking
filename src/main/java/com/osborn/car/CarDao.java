package com.osborn.car;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarDao {

    public Optional<Car> getCar(UUID carId);
    public List<Car> getCars();
    public Integer addCar(Car car);
    public void saveCars();
}
