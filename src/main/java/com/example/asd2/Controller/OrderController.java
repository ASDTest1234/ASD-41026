package com.example.asd2.Controller;

import com.example.asd2.Model.Cart;
import com.example.asd2.Model.Order;
import com.example.asd2.Service.CartNotFoundException;
import com.example.asd2.Service.CartService;
import com.example.asd2.Service.OrderService;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Controller
@RequestMapping("/api/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    /**
     * Shows the payment and shipping page based on customerId.
     * @param payload A map containing the customerId.
     * @param model The model to pass attributes to the Thymeleaf view.
     * @return The payment and shipping page view.
     */
    @PostMapping("/payment")
    public String showPaymentPage(@RequestBody Map<String, String> payload, Model model) {
        String customerId = payload.get("customerId");
        if (customerId == null) {
            logger.warn("No customerId found in the request payload.");
            model.addAttribute("error", "Customer ID is required.");
            return "errorPage";
        }
        logger.info("Navigating to Payment and Shipping Details page for customerId={}", customerId);
        model.addAttribute("customerId", customerId);
        return "Payment&ShippingDetails";
    }

    /**
     * Confirms and completes an order. It validates the order payload and then processes the order.
     * @param payload A map containing all necessary order details.
     * @param model The model to pass attributes to the Thymeleaf view.
     * @return The confirmation page or the payment page with errors.
     */
    @PostMapping("/confirm")
    public String confirmOrder(@RequestBody Map<String, Object> payload, Model model) {
        String customerId = (String) payload.get("customerId");
        String cardNumber = (String) payload.get("cardNumber");
        String expiryDate = (String) payload.get("expiryDate");
        String cvv = (String) payload.get("cvv");
        String address = (String) payload.get("address");
        String fullName = (String) payload.get("fullName");
        String city = (String) payload.get("city");
        String zipCode = (String) payload.get("zipCode");

        // Validate input data
        if (customerId == null || cardNumber == null || expiryDate == null || cvv == null || address == null || fullName == null || city == null || zipCode == null) {
            model.addAttribute("error", "All fields are required to complete the order.");
            logger.warn("Missing fields in the order confirmation payload.");
            return "Payment&ShippingDetails";
        }

        try {
            Cart cart = cartService.getCartByCustomerId(customerId);
            if (cart == null || cart.getItems().isEmpty()) {
                logger.warn("No items in cart for customerId={}", customerId);
                model.addAttribute("error", "No items in cart to complete order.");
                return "Payment&ShippingDetails";
            }

            // Create order document with customer details
            Document customerDetails = new Document()
                    .append("fullName", fullName)
                    .append("address", address)
                    .append("city", city)
                    .append("zipCode", zipCode)
                    .append("cardNumber", "****" + cardNumber.substring(cardNumber.length() - 4))
                    .append("expiryDate", expiryDate);

            // Create the order and clear the cart
            orderService.createOrder(customerId, cart.getItems(), customerDetails);
            cartService.clearCart(customerId);
            logger.info("Order completed successfully for customerId={}", customerId);
            return "orderConfirmation";
        } catch (CartNotFoundException e) {
            logger.error("Cart not found for customerId={}: {}", customerId, e.getMessage());
            model.addAttribute("error", "Cart not found.");
            return "Payment&ShippingDetails";
        } catch (Exception e) {
            logger.error("Order completion failed for customerId={}: {}", customerId, e.getMessage());
            model.addAttribute("error", "Order completion failed: " + e.getMessage());
            return "Payment&ShippingDetails";
        }
    }

    /**
     * Lists all orders for a specific customer.
     * @param customerId The customer ID for whom the orders are being fetched.
     * @return The Thymeleaf template for displaying user orders.
     */
    @GetMapping("/list")
    @ResponseBody  // This ensures the data is returned as JSON, not as a view
    public ResponseEntity<List<Order>> listOrdersForUser(@RequestParam String customerId) {
        logger.info("Fetching orders for customerId={}", customerId);
        List<Order> orders = orderService.getOrdersByCustomerId(customerId);
        return new ResponseEntity<>(orders, HttpStatus.OK);  // Return orders as JSON
    }


    /**
     * Displays details of a specific order.
     * @param orderId The order ID to fetch details.
     * @param model Model to pass data to the view.
     * @return The Thymeleaf template for order details.
     */
    @GetMapping("/orderDetails")
    public String orderDetails(@RequestParam("orderId") String orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            model.addAttribute("error", "Order not found.");
            return "errorPage";
        }

        Date estimatedDeliveryDate = new Date(order.getOrderDate().getTime() + (9 * 24 * 60 * 60 * 1000L));
        model.addAttribute("order", order);
        model.addAttribute("estimatedDeliveryDate", estimatedDeliveryDate);


        return "orderDetails";
    }

}
