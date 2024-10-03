package com.example.asd2.repository;

import com.example.asd2.Model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
    // Custom query to find a cart by customerId
    Cart findByCustomerId(String customerId);
}
