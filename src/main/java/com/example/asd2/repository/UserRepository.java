package com.example.asd2.repository;


import com.example.asd2.Model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.security.core.userdetails.User;

public interface UserRepository extends MongoRepository<Users, String> { // to interact with the mongoDB


    Users findByEmail(String email);

    Users findByPassword(String password);

    Users findByRole(String role);
}
