package com.example.asd2.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Document(collection = "cart")
public class Cart {

    @Id
    private String id;
    private String customerId;
    private List<Product> products;
    private double totalPrice;
}
