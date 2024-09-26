package com.example.asd2.Service;

import com.example.asd2.Model.Users;
import com.example.asd2.repository.UserRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

//    public Optional<Users> getUserByEmail(String email) {
//        return userRepository.findByEmail(email);
//    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<Users> getUserByID(String userID){
        return userRepository.findByUserID(userID);
    }
//
//    public Optional<Users> aEmail(String Email){
//        return userRepository.findByEmail(Email);
//    }
}