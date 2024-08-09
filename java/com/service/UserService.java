package com.service;

import com.model.Userr;

import java.util.List;

public interface UserService {

    Userr getUserById(int userId);

    Userr getUserByEmail(String email);

    List<Userr> getAllUsers();

    Userr saveUser(Userr user);

    Userr updateUser(Userr user);

    void deleteUser(int userId);
}
