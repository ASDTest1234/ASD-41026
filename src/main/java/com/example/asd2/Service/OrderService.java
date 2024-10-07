package com.example.asd2.Service;

import com.example.asd2.Model.Cart;
import com.example.asd2.Model.Products;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Service for managing Order operations.
 */
@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final MongoTemplate mongoTemplate;
    private final ProductService productService;

    @Autowired
    public OrderService(MongoTemplate mongoTemplate, ProductService productService) {
        this.mongoTemplate = mongoTemplate;
        this.productService = productService;
    }

    /**
     * Creates an order with customer and cart item details.
     *
     * @param customerId - the ID of the customer
     * @param items - the list of items in the cart
     * @param customerDetails - the customer's payment and shipping details
     * @throws Exception if any product has insufficient stock
     */
    public void createOrder(String customerId, List<Cart.CartItem> items, Document customerDetails) throws Exception {
        logger.info("Creating order for customerId: {} with items: {}", customerId, items);

        for (Cart.CartItem item : items) {
            Products product = productService.getProductByName(item.getProductName())
                    .orElseThrow(() -> new Exception("Product not found: " + item.getProductName()));

            if (product.getProductStock() < item.getQuantity()) {
                throw new Exception("Insufficient stock for product: " + item.getProductName());
            }
        }

        List<Document> orderItems = new ArrayList<>();
        for (Cart.CartItem item : items) {
            Document orderItemDoc = new Document()
                    .append("productName", item.getProductName())
                    .append("productDescription", item.getProductDescription())
                    .append("productPrice", item.getProductPrice())
                    .append("quantity", item.getQuantity());
            orderItems.add(orderItemDoc);
        }

        // Ensure all data is correct before insertion
        if (items.isEmpty()) {
            throw new Exception("No items to create an order.");
        }

        Document orderDoc = new Document()
                .append("orderNumber", UUID.randomUUID().toString())
                .append("customerId", customerId)
                .append("orderDate", new Date())
                .append("customerDetails", customerDetails)
                .append("items", orderItems)
                .append("totalPrice", items.stream().mapToDouble(i -> i.getProductPrice() * i.getQuantity()).sum());

        // Insert order to MongoDB
        mongoTemplate.insert(orderDoc, "Orders");
        logger.info("Order created successfully for customerId: {}", customerId);
    }

}
