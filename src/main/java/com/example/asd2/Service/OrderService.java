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

    public void createOrder(String customerId, List<Cart.CartItem> items) throws Exception {
        logger.info("Creating order for customerId: {} with items: {}", customerId, items);

        for (Cart.CartItem item : items) {
            Products product = productService.getProductByName(item.getProductName())
                    .orElseThrow(() -> new Exception("Product not found: " + item.getProductName()));

            if (product.getProductStock() < item.getQuantity()) {
                throw new Exception("Insufficient stock for product: " + item.getProductName());
            }
        }

        for (Cart.CartItem item : items) {
            Products product = productService.getProductByName(item.getProductName()).get();
            productService.updateProductStock(product.getProduct_Id(), product.getProductStock() - item.getQuantity());
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

        Document orderDoc = new Document()
                .append("customerId", customerId)
                .append("orderDate", new Date())
                .append("items", orderItems)
                .append("totalPrice", items.stream().mapToDouble(i -> i.getProductPrice() * i.getQuantity()).sum());

        mongoTemplate.insert(orderDoc, "Orders");
        logger.info("Order created successfully for customerId: {}", customerId);
    }
}
