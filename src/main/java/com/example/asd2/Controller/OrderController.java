package com.example.asd2.Controller;

import com.example.asd2.Model.Cart;
import com.example.asd2.Service.CartNotFoundException;
import com.example.asd2.Service.CartService;
import com.example.asd2.Service.OrderService;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/api/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    /**
     * Handles POST request to show payment page.
     * @param payload the request body containing the customerId
     * @param model the model to pass attributes to the view
     * @return the payment and shipping details page view name
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
     * Handles POST request to confirm and complete an order.
     * @param payload the request body containing order details
     * @param model the model to pass attributes to the view
     * @return the view name for either the order confirmation or payment page (if errors occur)
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

        if (customerId == null || cardNumber == null || expiryDate == null || cvv == null || address == null || fullName == null || city == null || zipCode == null) {
            model.addAttribute("error", "All fields are required to complete the order.");
            logger.warn("Missing fields in the order confirmation payload.");
            return "Payment&ShippingDetails";
        }

        logger.info("Attempting to confirm order for customerId={}", customerId);

        try {
            Cart cart = cartService.getCartByCustomerId(customerId);
            if (cart == null || cart.getItems().isEmpty()) {
                logger.warn("No items in cart for customerId={}", customerId);
                model.addAttribute("error", "No items in cart to complete order.");
                return "Payment&ShippingDetails";
            }

            Document customerDetails = new Document()
                    .append("fullName", fullName)
                    .append("address", address)
                    .append("city", city)
                    .append("zipCode", zipCode)
                    .append("cardNumber", "****" + cardNumber.substring(cardNumber.length() - 4))
                    .append("expiryDate", expiryDate);

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
}
