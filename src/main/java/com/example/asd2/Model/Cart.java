package com.example.asd2.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Document(collection = "Cart")
public class Cart {

    @Id
    private String id;
    private String customerId;
    private List<CartItem> items;
    private double totalPrice;

    @Getter
    @Setter
    public static class CartItem {
        private String productId;
        private String productName;
        private String productDescription;
        private double productPrice;
        private int quantity;  // 这里记录商品数量
    }
}
