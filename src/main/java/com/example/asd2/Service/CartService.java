package com.example.asd2.Service;

import com.example.asd2.Model.Cart;
import com.example.asd2.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    /**
     * Fetches the cart for the specified customer by their customer ID.
     *
     * @param customerId The ID of the customer.
     * @return Cart object containing the list of products and the total price.
     */
    public Cart getCartByCustomerId(String customerId) {
        return cartRepository.findByCustomerId(customerId);
    }
}
