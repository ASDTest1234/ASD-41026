package com.example.asd2.Service;

import com.example.asd2.Model.Users;
import com.example.asd2.repository.ProductRepository;
import com.example.asd2.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves a user by their email.
     * @param email - the email of the user to find
     * @return Optional containing the user if found
     */
    public Optional<Users> getUserByEmail(String email) {
        logger.info("Attempting to retrieve user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    /**
     * Retrieves a user by their userID.
     * @param userID - the user ID to find
     * @return Optional containing the user if found
     */
    public Optional<Users> getUserByID(String userID){
        logger.info("Attempting to retrieve user by userID: {}", userID);
        return userRepository.findByUserID(userID);
    }

//    public List<Users> getAllUsers(){
//        logger.info("Attempting to retrieve all users");
//        return userRepository.findAllUsers();
//    }

    public List<Users> getUsersByRoles(String filter){
        return userRepository.findUsersByRole(filter);
    }
    /**
     * Updates the user details in the database.
     *
     * @param updatedUser The user object containing updated details.
     */
    public void updateUser(Users updatedUser) {
        // Check if the user exists
        Users existingUser = userRepository.findById(updatedUser.getUserID()).orElseThrow(() -> new RuntimeException("User not found"));

        // Update the user's details
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhone(updatedUser.getPhone());
        existingUser.setAddress(updatedUser.getAddress());

        // Save the updated user back to the database
        userRepository.save(existingUser);
    }
}
