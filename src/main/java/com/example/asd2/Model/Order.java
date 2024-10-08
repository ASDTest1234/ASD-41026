package com.example.asd2.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(collection = "Orders")
public class Order {

    @Id
    private String orderId;
    private String customerId;
    private String orderNumber;
    private Date orderDate;
    private List<OrderItem> items;
    private double totalPrice;
    private CustomerDetails customerDetails;

    @Getter
    @Setter
    public static class OrderItem {
        private String productName;
        private int quantity;
    }

    @Getter
    @Setter
    public static class CustomerDetails {
        private String name;
        private String address;
    }
}
