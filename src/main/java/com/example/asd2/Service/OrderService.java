package com.example.asd2.Service;

import com.example.asd2.Model.Order;
import com.example.asd2.Model.Products;
import com.example.asd2.Model.Cart;
import com.example.asd2.repository.OrderRepository;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing Order operations.
 */
@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    /**
     * Creates an order with customer and cart item details.
     *
     * @param customerId - the ID of the customer
     * @param items - the list of items in the cart
     * @param customerDetails - the customer's payment and shipping details
     * @throws Exception if any product has insufficient stock or cannot be found
     */
    public void createOrder(String customerId, List<Cart.CartItem> items, Document customerDetails) throws Exception {
        logger.info("Creating order for customerId: {} with items: {}", customerId, items);

        // Check stock availability and retrieve product prices dynamically
        List<Order.OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;

        for (Cart.CartItem cartItem : items) {
            Optional<Products> productOpt = productService.getProductByName(cartItem.getProductName());

            if (productOpt.isPresent()) {
                Products product = productOpt.get();

                // Check if there's enough stock
                if (product.getProductStock() < cartItem.getQuantity()) {
                    throw new Exception("Insufficient stock for product: " + cartItem.getProductName());
                }

                // Deduct stock for the product
                productService.updateProductStock(product.getProductName(), product.getProductStock() - cartItem.getQuantity());

                // Create an OrderItem and dynamically set the current product price
                Order.OrderItem orderItem = new Order.OrderItem();
                orderItem.setProductName(product.getProductName());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setProductPrice(product.getProductPrice().doubleValue());  // Use the current price

                // Calculate total price for this item
                totalPrice += orderItem.getProductPrice() * orderItem.getQuantity();

                orderItems.add(orderItem);
            } else {
                throw new Exception("Product not found: " + cartItem.getProductName());
            }
        }

        // Create the order
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setCustomerId(customerId);
        order.setOrderNumber(UUID.randomUUID().toString());  // Generate a unique order number
        order.setOrderDate(new Date());
        order.setItems(orderItems);
        order.setTotalPrice(totalPrice);
        order.setCustomerDetails(new Order.CustomerDetails(
                customerDetails.getString("fullName"),
                customerDetails.getString("address"),
                customerDetails.getString("city"),
                customerDetails.getString("zipCode")
        ));
        order.setStatus(Order.OrderStatus.PREPARING);

        // Save the order to the repository
        orderRepository.save(order);
        logger.info("Order created successfully for customerId: {}", customerId);
    }

    /**
     * Fetch all orders for a specific customer based on the customerId.
     *
     * @param customerId The ID of the customer whose orders we want to retrieve.
     * @return List of orders.
     */
    public List<Order> getOrdersByCustomerId(String customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    /**
     * Fetch the details of a specific order based on the orderId.
     *
     * @param orderId The ID of the order to view details.
     * @return Order details or null if not found.
     */
    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
