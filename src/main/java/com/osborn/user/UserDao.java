package com.osborn.user;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    public List<User> getUsers();

    public Integer addUser(User user);

    public void saveUsers();

    public Optional<User> deleteUser(Integer userKey);
}
