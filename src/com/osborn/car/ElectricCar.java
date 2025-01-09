package com.osborn.car;

import java.util.UUID;

public class ElectricCar extends Car{
    public ElectricCar(UUID carId, UUID userId, boolean booked) {
        super(carId, userId, booked);
    }
}
