package com.example.asd2.repository;

import com.example.asd2.Model.RegisterUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RegisterUserRepository extends MongoRepository<RegisterUser, String> {

    /**
     * Find a user by their email.
     *
     * @param email The email of the user to find.
     * @return An Optional containing the user if found, or empty otherwise.
     */
    Optional<RegisterUser> findByEmail(String email);

    /**
     * Check if a user exists by email.
     *
     * @param email The email to check for existence.
     * @return true if a user with the given email exists, false otherwise.
     */
    boolean existsByEmail(String email);

    /**
     * Custom query to find users by a specific role and sort based on the filter.
     *
     * @param filter The role filter (e.g., admin, user, etc.).
     * @return A list of users whose roles match the given filter.
     */
    @Query("{ 'role': { $regex: ?0 }}")
    List<RegisterUser> findUsersByRole(String filter);
}

