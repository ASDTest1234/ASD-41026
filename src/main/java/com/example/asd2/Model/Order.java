package com.example.asd2.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(collection = "orders")
public class Order {

    @Id
    private String orderId;
    private String customerId;
    private Date orderDate;
    private List<OrderItem> items;

    @Getter
    @Setter
    public static class OrderItem {
        private String productName;
        private int quantity;
    }
}   
