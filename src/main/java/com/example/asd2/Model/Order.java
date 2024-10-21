package com.example.asd2.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Order entity representing customer orders.
 */
@Getter
@Setter
@Document(collection = "Orders")
public class Order {

    @Id
    private String orderId;  // Unique identifier for each order
    private String customerId;  // ID of the customer who placed the order
    private String orderNumber;  // A unique order number
    private Date orderDate;  // Date when the order was placed
    private List<OrderItem> items;  // List of items in the order
    private double totalPrice;  // Total price for the order
    private CustomerDetails customerDetails;  // Shipping and billing details of the customer
    private OrderStatus status;  // Current status of the order (e.g., PREPARING, SHIPPED)

    /**
     * Nested class representing individual items in an order.
     */
    @Getter
    @Setter
    public static class OrderItem {
        private String productName;  // Name of the product
        private int quantity;  // Quantity of the product ordered
        private double productPrice;  // Price of the product  (new field to store price)
    }

    /**
     * Nested class representing customer details for shipping and billing.
     */
    @Getter
    @Setter
    public static class CustomerDetails {
        private String fullName;  // Full name of the customer
        private String address;  // Shipping address
        private String city;  // City
        private String zipCode;  // Zip code

        // Constructor
        public CustomerDetails(String fullName, String address, String city, String zipCode) {
            this.fullName = fullName;
            this.address = address;
            this.city = city;
            this.zipCode = zipCode;
        }
    }

    /**
     * Enum representing the various statuses an order can have.
     */
    public enum OrderStatus {
        PREPARING,
        SHIPPED,
        DELIVERED,
        CANCELLED
    }
}
