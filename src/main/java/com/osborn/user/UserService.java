package com.osborn.user;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getUsers(){
        return userDao.getUsers();
    }

    public User createUser(String firstName, String lastName) {
        return new User(firstName, lastName);
    }

    public Integer addUser(User user) {
        return userDao.addUser(user);
    }

    public void save() {
        userDao.saveUsers();
    }

    public Optional<User> deleteUser(Integer userKey) {
        return userDao.deleteUser(userKey);
    }
}
