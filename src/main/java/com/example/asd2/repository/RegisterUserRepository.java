package com.example.asd2.Repository;

import com.example.asd2.Model.RegisterUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterUserRepository extends MongoRepository<RegisterUser, String> {
    // Custom query methods can be defined here if needed
}
