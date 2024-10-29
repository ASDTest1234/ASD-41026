package com.example.asd2.repository;

import com.example.asd2.Model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Repository for performing CRUD operations on the 'Orders' collection in MongoDB.
 */
@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    /**
     * Finds all orders for a specific customer by their customerId.
     * @param customerId The ID of the customer whose orders are being fetched.
     * @return A list of orders placed by the specified customer.
     */
    List<Order> findByCustomerId(String customerId);

}
