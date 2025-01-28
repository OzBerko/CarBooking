package com.osborn.car;

import java.io.*;
import java.util.*;

public class CarArrayDataAccessService implements CarDao {

    private final static File file = new File("src/main/resources/cars.csv");

    private final static Map<Integer, Car> cars = new HashMap<>();
    private static int carsIndex = 0;

    static {
        loadCarsFromFile();
    }

    private static void loadCarsFromFile() {
        try (Scanner scanner = new Scanner(file)) {
            if (!scanner.hasNextLine()) {
                System.out.println("Empty car file: No valid cars found in the file.");
            }
            while (scanner.hasNextLine()) {
                String inputCar = scanner.nextLine();
                Optional<Car> carOpt = createCar(inputCar);

                if (carOpt.isPresent()) {
                    cars.put(carsIndex, carOpt.get());
                    System.out.println("Car added: " + cars.get(carsIndex));
                    carsIndex++;
                } else {
                    System.out.println("Invalid car data, skipping: " + inputCar);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error occurred while loading cars: " + e.getMessage());
        }
    }

    private static Optional<Car> createCar(String inputCar) {
        String[] carFeats = inputCar.split(",");
        if (carFeats.length < 3) {
            return Optional.empty();
        }

        String carIdStr = carFeats[0].trim();
        Optional<UUID> carUuid = Car.validateUuidFormat(carIdStr);

        if (carUuid.isPresent()) {
            return Optional.of(new Car(carUuid.get()));
        } else {
            System.out.println("Failed to create car due to invalid UUID");
        }
        return Optional.empty();
    }


    @Override
    public Optional<Car> getCar(UUID carId) {
        Optional<Car> selectedCar = cars.values()
                                        .stream()
                                        .filter(car -> car != null && carId.equals(car.getCarId()))
                                        .findFirst();
        if (selectedCar.isPresent()) {
            return selectedCar;
        } else {
            System.out.println("No car found with the given ID: " + carId);
            return Optional.empty();
        }
    }

    @Override
    public List<Car> getCars() {
        return new ArrayList<>(cars.values());
    }

    @Override
    public Integer addCar(Car car) {
        cars.put(carsIndex, car);
        System.out.println("A new car has been added");
        Integer key = carsIndex;
        carsIndex++;
        return key;
    }

    @Override
    public void saveCars() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (Car car : cars.values()) {
                writer.write(car.getCarId() + ", " + car.getUserId() + ", " + car.isBooked());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
