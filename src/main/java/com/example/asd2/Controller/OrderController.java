package com.example.asd2.Controller;

import com.example.asd2.Model.Cart;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    /**
     * Handles GET request to show payment and shipping details page.
     * @param customerId the ID of the customer
     * @param model the model to pass attributes to the view
     * @return the payment and shipping details page view name
     */
    @GetMapping("/payment")
    public String showPaymentPage(@RequestParam String customerId, Model model) {
        logger.info("Navigating to Payment and Shipping Details page for customerId={}", customerId);

        model.addAttribute("customerId", customerId); // Pass customerId to the view
        return "Payment&ShippingDetails"; // Return template name
    }


    /**
     * Handles POST request to confirm and complete an order.
     * @param customerId the ID of the customer
     * @param cardNumber the card number for payment
     * @param expiryDate the expiry date of the card
     * @param cvv the CVV code of the card
     * @param address the shipping address
     * @param model the model to pass attributes to the view
     * @return the view name for either the order confirmation or the payment page (if errors occur)
     */
    @PostMapping("/confirm")
    public String confirmOrder(@RequestParam String customerId,
                               @RequestParam String cardNumber,
                               @RequestParam String expiryDate,
                               @RequestParam String cvv,
                               @RequestParam String address,
                               @RequestParam String fullName,
                               @RequestParam String city,
                               @RequestParam String zipCode,
                               Model model) {

        logger.info("Attempting to confirm order for customerId={}", customerId); // Log order attempt
        try {
            Cart cart = cartService.getCartByCustomerId(customerId);
            if (cart == null || cart.getItems().isEmpty()) {
                logger.warn("No items in cart for customerId={}", customerId); // Warn if cart is empty
                model.addAttribute("error", "No items in cart to complete order.");
                return "Payment&ShippingDetails";
            }

            // Create a Document for customer details
            Document customerDetails = new Document()
                    .append("fullName", fullName)
                    .append("address", address)
                    .append("city", city)
                    .append("zipCode", zipCode)
                    .append("cardNumber", "****" + cardNumber.substring(cardNumber.length() - 4)) // Mask card number
                    .append("expiryDate", expiryDate);

            // Simulate payment processing (logging only, no real processing)
            logger.info("Processing payment for customerId={}, cardNumber=****{}", customerId, cardNumber.substring(cardNumber.length()-4));

            // Pass customerId, cart items, and customerDetails to createOrder
            orderService.createOrder(customerId, cart.getItems(), customerDetails);

            // Clear the cart after successful order
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