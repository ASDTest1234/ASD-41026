package com.example.asd2.repository;

import com.example.asd2.Model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {

    /**
     * Finds the cart for a given customer by customer ID.
     *
     * @param customerId The ID of the customer.
     * @return Cart object associated with the customer.
     */
    Cart findByCustomerId(String customerId);
}
