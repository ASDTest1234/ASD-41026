package com.example.asd2.Controller;

import com.example.asd2.Model.Users;
import com.example.asd2.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user/userDetails")
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
    /**
     * Get user details for the currently authenticated user.
     * @param authentication - the authentication object
     * @return ResponseEntity with user details or error message
     */
    @GetMapping("/details")
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            logger.info("Fetching user details for email: {}", email);

            Optional<Users> userOpt = userService.getUserByEmail(email);
            if (userOpt.isPresent()) {
                return new ResponseEntity<>(userOpt.get(), HttpStatus.OK);
            } else {
                logger.warn("User not found for email: {}", email);
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        }
        logger.warn("User is not authenticated");
        return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/update_details")
    public String getUpdateDetailsPage() {
        return "update_details"; // This will return the update_details.html template
    }

    @PostMapping("/update_details")
    public ResponseEntity<String> updateUserDetails(@RequestBody Users updatedUser) {
        userService.updateUser(updatedUser);
        return ResponseEntity.ok("User details updated successfully");
    }

    @GetMapping("/confirm_delete")
    public ResponseEntity<String> getConfirmDeletePage() {
        return new ResponseEntity<>("Confirm delete page (HTML not served here).", HttpStatus.OK);
    }
}
