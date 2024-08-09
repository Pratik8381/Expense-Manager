package com.service;

import com.model.Userr;
import com.dao.UserrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserrRepository userRepository;

    @Autowired
    public UserServiceImpl(UserrRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Userr getUserById(int userId) {
        Optional<Userr> userOptional = userRepository.findById(userId);
        return userOptional.orElse(null);
    }

    @Override
    public Userr getUserByEmail(String email) {
        return userRepository.findByUemail(email);
    }

    @Override
    public List<Userr> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Userr saveUser(Userr user) {
        return userRepository.save(user);
    }

    @Override
    public Userr updateUser(Userr user) {
        // Check if the user exists in the database
        Optional<Userr> existingUserOptional = userRepository.findById(user.getUid());
        if (existingUserOptional.isPresent()) {
            return userRepository.save(user);
        } else {
            // Handle case where user doesn't exist
            throw new RuntimeException("User not found with id: " + user.getUid());
        }
    }

    @Override
    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }
}
