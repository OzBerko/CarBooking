package com.osborn.user;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> getUsers();

    Integer addUser(User user);

    void saveUsers();

    Optional<User> deleteUser(Integer userKey);
}
