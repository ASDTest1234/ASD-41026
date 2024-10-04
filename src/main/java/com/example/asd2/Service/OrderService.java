package com.example.asd2.Service;

import com.example.asd2.Model.Cart;
import com.example.asd2.Model.Products;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private ProductService productService;
    private final MongoCollection<Document> orderCollection;

    @Autowired
    public OrderService(MongoDatabase database) {
        this.orderCollection = database.getCollection("Orders");
    }

    public void createOrder(String customerId, List<Cart.CartItem> items) throws Exception {
        logger.info("Creating order for customerId: {} with items: {}", customerId, items);

        for (Cart.CartItem item : items) {
            Products product = productService.getProductByName(item.getProductName())
                    .orElseThrow(() -> new Exception("Product not found: " + item.getProductName()));

            if (product.getProductStock() < item.getQuantity()) {
                logger.error("Insufficient stock for product: {}. Available: {}, Required: {}", item.getProductName(), product.getProductStock(), item.getQuantity());
                throw new Exception("Insufficient stock for product: " + item.getProductName());
            }
        }

        for (Cart.CartItem item : items) {
            Products product = productService.getProductByName(item.getProductName()).get();
            int newStock = product.getProductStock() - item.getQuantity();
            productService.updateProductStock(product.getProduct_Id(), newStock);
            logger.debug("Updated stock for product: {}. New stock: {}", product.getProductName(), newStock);
        }

        Document orderDoc = new Document()
                .append("customerId", customerId)
                .append("orderDate", new Date())
                .append("items", items)
                .append("totalPrice", items.stream().mapToDouble(item -> item.getProductPrice() * item.getQuantity()).sum());

        orderCollection.insertOne(orderDoc);
        logger.info("Order created successfully for customerId: {} with order details: {}", customerId, orderDoc);
    }
}
