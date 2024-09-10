package com.example.asd2.Controller;

import com.example.asd2.Model.Cart;
import com.example.asd2.Model.Product;
import com.example.asd2.Service.CartService;
import com.example.asd2.Service.CartNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * Retrieves the cart details for a specific customer by their customer ID.
     *
     * @param customerId The ID of the customer whose cart is to be fetched.
     * @return ResponseEntity containing the Cart object or an error status.
     */
    @GetMapping("/cart/{customerId}")
    public ResponseEntity<Cart> getCartByCustomerId(@PathVariable String customerId) {
        try {
            Cart cart = cartService.getCartByCustomerId(customerId);
            return new ResponseEntity<>(cart, HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Clears the cart for a specific customer by customer ID.
     *
     * @param customerId The ID of the customer.
     * @return ResponseEntity with appropriate HTTP status.
     */
    @DeleteMapping("/cart/{customerId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable String customerId) {
        try {
            cartService.clearCart(customerId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Adds a product to the cart for a specific customer.
     *
     * @param customerId The ID of the customer.
     * @param product    The product to add.
     * @return ResponseEntity with appropriate HTTP status.
     */
    @PostMapping("/cart/{customerId}/add")
    public ResponseEntity<Void> addProductToCart(@PathVariable String customerId, @RequestBody Product product) {
        try {
            cartService.addProductToCart(customerId, product);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
