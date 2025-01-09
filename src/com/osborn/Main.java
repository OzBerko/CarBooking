package com.osborn;

import com.osborn.booking.BookingArrayDataAccessService;
import com.osborn.booking.BookingDao;
import com.osborn.booking.BookingService;
import com.osborn.car.*;
import com.osborn.transaction.CarBookingTransactionService;
import com.osborn.user.UserArrayDataAccessService;
import com.osborn.user.UserDao;
import com.osborn.user.UserService;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final int MIN_OPTION = 1;
    private static final int MAX_OPTION = 7;
    private static final Set<String> VALID_CITIES;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    static {
        VALID_CITIES = new HashSet<>();
        for (CityOfOperations city : CityOfOperations.values()) {
            VALID_CITIES.add(city.name());
        }
    }


    public static void main(String[] args) {
        UserDao userDbService = new UserArrayDataAccessService();
        UserService userService = new UserService(userDbService);

        CarDao carDbService = new CarArrayDataAccessService();
        CarService carService = new CarService(carDbService);

        BookingDao bookingDbService = new BookingArrayDataAccessService();
        BookingService bookingService = new BookingService(bookingDbService);

        CarBookingTransactionService transactionService = new CarBookingTransactionService(userService, carService,
                                                                                           bookingService);

        int validInteger;
        OptionalInt validNumber;
        while (true) {
            System.out.println(
                    """
                            
                            Please enter a valid number:
                            1️⃣ - Book Car
                            2️⃣ - View All User Booked Cars
                            3️⃣ - View All Bookings
                            4️⃣ - View Available Cars
                            5️⃣ - View Available Electric Cars
                            6️⃣ - View all users
                            7️⃣ - Exit""");

            String input = scanner.nextLine();
            validNumber = stringToOptionalInt(input);
            if (validNumber.isPresent()) {
                validInteger = validNumber.getAsInt();


                if (validInteger >= MIN_OPTION && validInteger <= MAX_OPTION) {
                    switch (validInteger) {
                        case 1 -> bookCar(transactionService, carService);
                        case 2 -> viewBookedCars(carService);
                        case 3 -> viewBookings(bookingService);
                        case 4 -> viewAvailableCars(carService);
                        case 5 -> viewAvailableECars(carService);
                        case 6 -> viewUsers(userService);
                        case 7 -> {
                            System.out.println("Exiting...");
                            System.exit(0);  // Exit the program
                        }
                    }
                } else {
                    System.out.println("Please enter a number between " + MIN_OPTION + " and " + MAX_OPTION + ".");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private static void bookCar(CarBookingTransactionService transactionService, CarService carService) {
        Optional<UUID> carIdOpt = getCar(carService);
        if (carIdOpt.isEmpty()){
            return;
        }
        String firstName = getInput("Please enter your first name:");
        String lastName = getInput("Please enter your last name:");
        ZonedDateTime[] zonedDateTimes = getDatesTimesInput();

        ZonedDateTime startDateTime = zonedDateTimes[0];
        ZonedDateTime endDateTime = zonedDateTimes[1];

        carIdOpt.ifPresent(uuid -> transactionService.bookCar(firstName, lastName, startDateTime, endDateTime, uuid));
    }

    private static <T> void addToArray(T[] arr, T element) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null) {
                arr[i] = element;
                System.out.println("A new " + element.getClass()
                                                     .getSimpleName() + " has been added");
                break;
            }

            System.out.println(element.getClass()
                                      .getSimpleName() + " is full.");

        }
    }

    private static String getInput(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }

    private static Optional<UUID> getCar(@NotNull CarService carService) {
        boolean availableCars = carService.availableCars();
        if (!availableCars) {
            System.out.println("No cars available.");
            return Optional.empty();
        }

        do {
            String carIdInput = getInput("Please enter the valid car id of your chosen car:");
            Optional<UUID> carIdOpt = Car.validateUuidFormat(carIdInput);
            if (carIdOpt.isEmpty()) {
                System.out.println("Invalid UUID format. Please try again.");
                continue;
            }
            return carIdOpt;
        } while (true);
    }

    private static CityOfOperations getCityOfOperations() {
        CityOfOperations city = null;

        while (city == null) {
            System.out.println("Please enter the city your booking is taking place in:");
            Arrays.stream(CityOfOperations.values())
                  .forEach(System.out::println);
            String cityInput = scanner.nextLine()
                                      .trim();

            if (VALID_CITIES.contains(cityInput)) {
                city = CityOfOperations.valueOf(cityInput);
            } else {
                System.out.println("Invalid city. Please choose from the list.");
            }
        }
        return city;
    }

    private static ZonedDateTime[] getDatesTimesInput() {

        ZonedDateTime zonedStartDateTime = null;
        ZonedDateTime zonedEndDateTime = null;

        while (zonedStartDateTime == null) {
            try {
                String startDate = getInput("Please enter the start date (yyyy-MM-dd) for your booking:");
                String endDate = getInput("Please enter the end date (yyyy-MM-dd) for your booking:");
                String startTime = getInput("Please enter the start time (HH:mm, 24-hour format) for your booking:");
                String endTime = getInput("Please enter the end time (HH:mm, 24-hour format) for your booking:");
                CityOfOperations city = getCityOfOperations();

                LocalDate localStartDate = LocalDate.parse(startDate, dateFormatter);
                LocalDate localEndDate = LocalDate.parse(endDate, dateFormatter);
                LocalTime localStartTime = LocalTime.parse(startTime);
                LocalTime localEndTime = LocalTime.parse(endTime);

                ZoneId zone = city.getTimeZone();
                zonedStartDateTime = ZonedDateTime.of(localStartDate, localStartTime, zone);
                zonedEndDateTime = ZonedDateTime.of(localEndDate, localEndTime, zone);

            } catch (DateTimeParseException e) {
                System.out.println("Invalid date/time format. Please check your inputs.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        return new ZonedDateTime[]{zonedStartDateTime, zonedEndDateTime};
    }

    private static void viewBookedCars(CarService carService) {
        carService.getCars()
                  .stream()
                  .filter(car -> car != null && car.isBooked())
                  .forEach(System.out::println);
    }

    private static void viewBookings(BookingService bookingService) {
        bookingService.getBookings()
                      .stream()
                      .filter(Objects::nonNull)
                      .forEach(System.out::println);
    }

    private static void viewAvailableCars(CarService carService) {
        carService.getCars()
                  .stream()
                  .filter(car -> car != null && !car.isBooked())
                  .forEach(System.out::println);
    }

    private static void viewAvailableECars(CarService carService) {
        carService.getCars()
                  .stream()
                  .filter(car -> car instanceof ElectricCar)
                  .forEach(System.out::println);
    }

    private static void viewUsers(UserService userService) {
        //    Note a for loop is better for small arrays
        userService.getUsers()
                   .stream()
                   .filter(Objects::nonNull)
                   .forEach(System.out::println);
    }

    public static OptionalInt stringToOptionalInt(String str) {
        if (str == null || str.isBlank()) {
            return OptionalInt.empty();
        }

        try {
            return OptionalInt.of(Integer.parseInt(str.trim()));
        } catch (NumberFormatException e) {
            return OptionalInt.empty();
        }
    }
}

