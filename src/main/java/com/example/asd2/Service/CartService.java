package com.example.asd2.Service;

import com.example.asd2.Model.Cart;
import com.example.asd2.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    // 获取指定 customerId 的购物车
    public Cart getCartByCustomerId(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        }

        Cart cart = cartRepository.findByCustomerId(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart not found for customer ID: " + customerId);
        }
        return cart;
    }
}
