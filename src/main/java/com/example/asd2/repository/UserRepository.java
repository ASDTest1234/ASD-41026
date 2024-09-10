package com.example.asd2.repository;

import com.example.asd2.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<Users, String> {
    Users findByEmail(String email);
}
