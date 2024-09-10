package com.example.asd2.controller;

import com.example.asd2.model.RegisterUser;
import com.example.asd2.service.RegisterUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/register")
public class RegisterUserController {

    @Autowired
    private RegisterUserService registerUserService;

    // Step 1: Basic details submission
    @PostMapping("/step1")
    public ResponseEntity<RegisterUser> handleStep1(@RequestParam String fName, @RequestParam String lName, 
                                             @RequestParam String dob, @RequestParam String email, 
                                             @RequestParam String phone, @RequestParam String password) {
        RegisterUser user = new RegisterUser();
        user.setfName(fName);
        user.setlName(lName);
        user.setDob(dob);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password);

        RegisterUser savedUser = registerUserService.createUser(user);  // Save the user
        return ResponseEntity.ok(savedUser);
    }

    // Step 2: Address details submission
    @PostMapping("/step2")
    public ResponseEntity<Void> handleStep2(@RequestParam String state, @RequestParam String city,
                                            @RequestParam String suburb, @RequestParam String postcode, 
                                            @RequestParam String streetName, @RequestParam String streetNumber,
                                            @RequestParam(required = false) String unitNumber) {
        // Retrieve user by email (replace with actual session management)
        String email = "john.doe@example.com";  // Placeholder for demo
        RegisterUser user = registerUserService.getUserByEmail(email);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Update user with address details
        user.setState(state);
        user.setCity(city);
        user.setSuburb(suburb);
        user.setPostcode(postcode);
        user.setStreetName(streetName);
        user.setStreetNumber(streetNumber);
        user.setUnitNumber(unitNumber);

        registerUserService.updateUser(user.get_id(), user);

        // Redirect to login.html after successful registration
        return ResponseEntity.status(HttpStatus.FOUND)
                             .location(URI.create("/login.html"))
                             .build();
    }
}
