package com.example.asd2.Model;

import lombok.Getter;
import lombok.Setter;

@Getter // Lombok generates getters for all fields
@Setter // Lombok generates setters for all fields
public class Product {

    private String productId;           // Product identifier
    private String productName;         // Product name
    private String productDescription;  // Description of the product
    private double productPrice;        // Price of the product

    // Lombok will automatically generate getter and setter methods
}
