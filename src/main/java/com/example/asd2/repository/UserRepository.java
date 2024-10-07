package com.example.asd2.repository;

import com.example.asd2.Model.Products;
import com.example.asd2.Model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<Users, String> {

    /**
     * Find a user by their userID.
     *
     * @param userID The userID of the user to find.
     * @return An Optional containing the user if found, or empty otherwise.
     */
    Optional<Users> findByUserID(String userID);

    /**
     * Find a user by their username.
     *
     * @param username The username of the user to find.
     * @return An Optional containing the user if found, or empty otherwise.
     */
    Optional<Users> findByUsername(String username);

    /**
     * Find a user by their email.
     *
     * @param email The email of the user to find.
     * @return An Optional containing the user if found, or empty otherwise.
     */
    Optional<Users> findByEmail(String email);



    @Query("{ 'role': { $regex: ?0 }}")
    List<Users> findUsersByRole(String filter);

}
