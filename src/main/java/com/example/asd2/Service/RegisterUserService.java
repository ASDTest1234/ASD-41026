package com.example.asd2.Service;

import com.example.asd2.Model.RegisterUser;
import com.example.asd2.repository.RegisterUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegisterUserService {

    private final RegisterUserRepository registerUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterUserService(RegisterUserRepository registerUserRepository, PasswordEncoder passwordEncoder) {
        this.registerUserRepository = registerUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Method to check if a user exists by email
    public Optional<RegisterUser> findByEmail(String email) {
        return registerUserRepository.findByEmail(email);
    }

    // Method to register a new user
    public RegisterUser registerUser(RegisterUser user) {
        // Encrypt the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return registerUserRepository.save(user);
    }
}
