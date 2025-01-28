package com.osborn.user;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class User {
    private String firstName;
    private String lastName;
    private UUID userId;

    public User(String firstName, String lastName, UUID userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = UUID.randomUUID();
    }

    public static Optional<UUID> validateUuid(String userIdStr) {
        try {
            return Optional.of(UUID.fromString(userIdStr));
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid User UUID format: " + userIdStr);
            return Optional.empty();
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, userId);
    }

    @Override
    public String toString() {
        return "User{" +
               "firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", userId=" + userId +
               '}';
    }
}
