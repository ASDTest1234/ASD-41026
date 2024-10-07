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

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

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

    @PostMapping("/updateQuantity")
    public ResponseEntity<String> updateProductQuantity(
            @RequestParam String customerId,
            @RequestParam String productName,
            @RequestParam int quantity) {
        try {
            cartService.updateProductQuantity(customerId, productName, quantity);
            return new ResponseEntity<>("Quantity updated successfully", HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>("Cart not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/removeProduct")
    public ResponseEntity<String> removeProductFromCart(@RequestParam String customerId, @RequestParam String productName) {
        try {
            cartService.removeProductFromCart(customerId, productName);
            return new ResponseEntity<>("Product removed successfully", HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>("Cart not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/complete")
    public ResponseEntity<String> completeOrder(@RequestParam String customerId) {
        try {
            Cart cart = cartService.getCartByCustomerId(customerId);
            if (cart == null || cart.getItems().isEmpty()) {
                return new ResponseEntity<>("No items in cart to complete order.", HttpStatus.BAD_REQUEST);
            }

            orderService.createOrder(customerId, cart.getItems());
            cartService.clearCart(customerId);

            return new ResponseEntity<>("Order completed successfully", HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Order completion failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
