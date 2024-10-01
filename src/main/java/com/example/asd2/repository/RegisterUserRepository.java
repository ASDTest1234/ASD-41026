package com.example.asd2.repository;

import com.example.asd2.Model.RegisterUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RegisterUserRepository extends MongoRepository<RegisterUser, String> {
    RegisterUser findByEmail(String email);
}
