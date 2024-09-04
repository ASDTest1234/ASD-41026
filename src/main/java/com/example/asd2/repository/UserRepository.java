package com.example.asd2.repository;


import com.example.asd2.Model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<Customer, String> {
    Optional<Customer> findByUsername(String username);
}