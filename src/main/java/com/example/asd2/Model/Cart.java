package com.example.asd2.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "Cart")
public class Cart {

    @Id
    private String id;
    private String customerId;
    private List<CartItem> items;
    private double totalPrice;
    private boolean stockSufficient; // Indicates if all items have sufficient stock

    @Getter
    @Setter
    public static class CartItem {
        private String productName;
        private String productDescription;
        private double productPrice;
        private int quantity;
        private String productType;
        private boolean stockSufficient; // Indicates if this item has sufficient stock
    }
}
