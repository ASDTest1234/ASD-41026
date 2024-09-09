package com.example.asd2.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter // Lombok generates getters for all fields
@Setter // Lombok generates setters for all fields
@Document(collection = "cart") // Specifies the MongoDB collection name as 'cart'
public class Cart {

    @Id
    private String id;                  // Unique identifier for the cart
    private String customerId;          // Customer identifier associated with the cart
    private List<Product> products;     // List of products in the cart
    private double totalPrice;          // Total price of all products in the cart

    // Lombok will automatically generate getter and setter methods
}
