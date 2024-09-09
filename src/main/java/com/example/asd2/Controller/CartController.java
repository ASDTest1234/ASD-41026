package com.example.asd2.Controller;

import com.example.asd2.Model.Cart;
import com.example.asd2.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * Retrieves the cart details for a specific customer by their customer ID.
     *
     * @param customerId The ID of the customer whose cart is to be fetched.
     * @return Cart object containing cart details.
     */
    @GetMapping("/cart/{customerId}")
    public Cart getCartByCustomerId(@PathVariable String customerId) {
        return cartService.getCartByCustomerId(customerId);
    }
}
