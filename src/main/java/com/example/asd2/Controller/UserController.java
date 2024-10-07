package com.example.asd2.Controller;

import com.example.asd2.Model.Users;
import com.example.asd2.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * Get current user ID based on their email stored in Authentication object.
     * @param authentication - the authentication object containing user's email
     * @return Map containing user ID if found, otherwise a default value.
     */
    @GetMapping("/currentUserId")
    public Map<String, String> getCurrentUserId(Authentication authentication) {
        Map<String, String> response = new HashMap<>();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            logger.info("Retrieving user ID for email: {}", email);

            Optional<Users> userOpt = userService.getUserByEmail(email);
            if (userOpt.isPresent()) {
                response.put("userId", userOpt.get().getUserID());
                logger.info("User ID found: {}", userOpt.get().getUserID());
            } else {
                response.put("userId", "Guest");
                logger.warn("No User ID found for email: {}", email);
            }
        } else {
            response.put("userId", "Guest");
        }
        return response;
    }
}
