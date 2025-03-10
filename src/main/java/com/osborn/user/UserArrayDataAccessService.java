package com.osborn.user;

import com.github.javafaker.Faker;

import java.io.*;
import java.util.*;

public class UserArrayDataAccessService implements UserDao {
    private final static String filePath = "target/runtime-files/users.csv";
    private final static File file = new File(filePath);
    //    private final static File file1 = new File(filePath);
//    private final static File file = new File(Objects.requireNonNull(UserArrayDataAccessService.class.getClassLoader()
//                                                                                                     .getResource(
//                                                                                                             "users.csv"))
//                                                     .getPath());

    private final static Map<Integer, User> users = new HashMap<>();

    private static Integer usersIndex = 0;

    static {
        generateUsersCsv();
        loadUsersFromFile();
        printFile();
    }

    private static void printFile() {
        System.out.println("\nUSER FILE CHECK:");
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private static void generateUsersCsv() {
        Faker faker = new Faker();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            for (int i = 0; i < 10; i++) {
                writer.write(UUID.randomUUID() + ", "
                             + faker.name()
                                    .firstName() + ", "
                             + faker.name()
                                    .lastName() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void loadUsersFromFile() {
        try (Scanner scanner = new Scanner(file)) {
            if (!scanner.hasNextLine()) {
                System.out.println("Empty users file: No valid users found in the file.");
            }
            while (scanner.hasNextLine()) {
                String inputUser = scanner.nextLine();
                Optional<User> userOpt = createUser(inputUser);

                if (userOpt.isPresent()) {
                    users.put(usersIndex, userOpt.get());
                    System.out.println("User added: " + users.get(usersIndex));
                    usersIndex++;
                } else {
                    System.out.println("Invalid user data, skipping: " + inputUser);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error occurred while loading users: " + e.getMessage());
        }
    }

    private static Optional<User> createUser(String inputUser) {
        String[] userFeats = inputUser.split(",");
        if (userFeats.length < 3) {
            return Optional.empty();
        }

        String userIdStr = userFeats[0].trim();
        String firstName = userFeats[1].trim();
        String lastName = userFeats[2].trim();

        Optional<UUID> userUuid = User.validateUuid(userIdStr);

        if (userUuid.isPresent()) {
            return Optional.of(new User(firstName, lastName, userUuid.get()));
        } else {
            System.out.println("Failed to create User due to invalid UUID");
        }
        return Optional.empty();
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Integer addUser(User user) {
        users.put(usersIndex, user);
        System.out.println("A new user has been added");
        Integer key = usersIndex;
        usersIndex++;
        return key;
    }

    @Override
    public void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (User user : users.values()) {
                writer.write(user.getUserId() + ", " + user.getFirstName() + ", " + user.getLastName());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    @Override
    public Optional<User> deleteUser(Integer userKey) {
        return Optional.ofNullable(users.remove(userKey));
    }
}
