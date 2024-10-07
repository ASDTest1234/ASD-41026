package com.example.asd2.Controller;

import com.example.asd2.Model.RegisterUser;
import com.example.asd2.Repository.RegisterUserRepository; // Make sure to create this repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
public class RegisterUserController {

    @Autowired
    private RegisterUserRepository registerUserRepository; // Assume this repository is defined

    @PostMapping
    public ResponseEntity<RegisterUser> registerUser(@RequestBody RegisterUser registerUser) {
        // Here, you can add validation logic for the registration data if needed

        // Save the registerUser object to the database
        RegisterUser savedUser = registerUserRepository.save(registerUser);

        // Return a response indicating success
        return ResponseEntity.ok(savedUser);
    }
}
