package com.example.asd2.Controller;

import com.example.asd2.Model.RegisterUser;
import com.example.asd2.Service.RegisterUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/register")
public class RegisterUserController {

    private final RegisterUserService registerUserService;

    @Autowired
    public RegisterUserController(RegisterUserService registerUserService) {
        this.registerUserService = registerUserService;
    }

    // Show registration form (GET request)
    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new RegisterUser());
        return "register"; // returns the register.html page
    }

    // Handle registration form submission (POST request)
    @PostMapping
public String registerUser(@ModelAttribute("user") RegisterUser user, Model model) {
    // Optionally, check if the email already exists
    if (registerUserService.findByEmail(user.getEmail()).isPresent()) {
        model.addAttribute("errorMessage", "Email already in use.");
        return "register"; // Redisplay the form with an error message
    }
    registerUserService.registerUser(user);
    return "redirect:/success"; 
}
}
