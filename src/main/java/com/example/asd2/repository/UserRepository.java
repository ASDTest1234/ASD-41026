package com.example.asd2.repository;

import com.example.asd2.Model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<Users, String> {
   Users findByEmail(String email);
    Optional<Users> findByUserID(String userID);
}
