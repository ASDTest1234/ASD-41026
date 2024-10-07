package com.example.asd2.Controller;

import com.example.asd2.Model.Cart;
import com.example.asd2.Service.CartNotFoundException;
import com.example.asd2.Service.CartService;
import com.example.asd2.Service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.bson.Document;


@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*") // Allow cross-origin requests for frontend access
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    /**
     * Retrieves the cart for a specific customer by their customer ID.
     * This method uses a GET request with a required customerId parameter.
     *
     * @param customerId - the ID of the customer whose cart is being retrieved
     * @return ResponseEntity containing the cart and a status of OK, or a status of NOT FOUND if the cart is missing
     */
    @GetMapping
    public ResponseEntity<Cart> getCart(@RequestParam String customerId) {
        try {
            Cart cart = cartService.getCartByCustomerId(customerId);
            return new ResponseEntity<>(cart, HttpStatus.OK);
        } catch (CartNotFoundException e) {
            logger.warn("Cart not found for customerId: {}", customerId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error fetching cart for customerId={}: {}", customerId, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the quantity of a specific product in the customer's cart.
     * This method expects customerId, productName, and quantity as request parameters.
     *
     * @param customerId  - the ID of the customer
     * @param productName - the name of the product to update
     * @param quantity    - the new quantity to set for the specified product
     * @return ResponseEntity with a message on success or an error status on failure
     */
    @PostMapping("/updateQuantity")
    public ResponseEntity<String> updateProductQuantity(
            @RequestParam String customerId,
            @RequestParam String productName,
            @RequestParam int quantity) {
        try {
            cartService.updateProductQuantity(customerId, productName, quantity); // Calls service to update quantity
            return new ResponseEntity<>("Quantity updated successfully", HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>("Cart not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error updating quantity for customerId={}, productName={}: {}", customerId, productName, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Removes a specific product from the customer's cart by product name.
     * This method expects customerId and productName as request parameters.
     *
     * @param customerId  - the ID of the customer
     * @param productName - the name of the product to remove from the cart
     * @return ResponseEntity with a success message or an error status on failure
     */
    @PostMapping("/removeProduct")
    public ResponseEntity<String> removeProductFromCart(@RequestParam String customerId, @RequestParam String productName) {
        try {
            cartService.removeProductFromCart(customerId, productName); // Calls service to remove the product
            return new ResponseEntity<>("Product removed successfully", HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>("Cart not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error removing product for customerId={}, productName={}: {}", customerId, productName, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/complete")
    public ResponseEntity<String> completeOrder(@RequestParam String customerId,
                                                @RequestParam String cardNumber,
                                                @RequestParam String expiryDate,
                                                @RequestParam String cvv,
                                                @RequestParam String address,
                                                @RequestParam String fullName,
                                                @RequestParam String city,
                                                @RequestParam String zipCode) {
        try {
            Cart cart = cartService.getCartByCustomerId(customerId);
            if (cart == null || cart.getItems().isEmpty()) {
                return new ResponseEntity<>("No items in cart to complete order.", HttpStatus.BAD_REQUEST);
            }

            // Create a Document for customer details
            Document customerDetails = new Document()
                    .append("fullName", fullName)
                    .append("address", address)
                    .append("city", city)
                    .append("zipCode", zipCode)
                    .append("cardNumber", "****" + cardNumber.substring(cardNumber.length() - 4)) // Mask card number
                    .append("expiryDate", expiryDate);

            // Create the order and clear the cart upon successful order creation
            orderService.createOrder(customerId, cart.getItems(), customerDetails);
            cartService.clearCart(customerId); // Clear the cart after the order is created

            logger.info("Order completed and cart cleared successfully for customerId={}", customerId);
            return new ResponseEntity<>("Order completed successfully", HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>("Cart not found for customerId: " + customerId, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Order completion failed for customerId={}: {}", customerId, e.getMessage());
            return new ResponseEntity<>("Order completion failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
