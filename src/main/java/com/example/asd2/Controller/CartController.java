package com.example.asd2.Controller;

import com.example.asd2.Model.Cart;
import com.example.asd2.Service.CartNotFoundException;
import com.example.asd2.Service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    // 默认用户 ID
    private static final String DEFAULT_USER_ID = "testUser123";

    // API 用于获取特定用户的购物车
    @GetMapping
    public ResponseEntity<Cart> getCart(@RequestParam(value = "customerId", defaultValue = DEFAULT_USER_ID) String customerId) {
        logger.info("Fetching cart for customerId={}", customerId);
        try {
            Cart cart = cartService.getCartByCustomerId(customerId);
            logger.info("Cart retrieved successfully for customerId={}: {}", customerId, cart);
            return new ResponseEntity<>(cart, HttpStatus.OK);
        } catch (CartNotFoundException e) {
            logger.warn("No cart found for customerId={}. Returning empty cart.", customerId);

            // 返回一个空购物车
            Cart emptyCart = new Cart();
            emptyCart.setCustomerId(customerId);
            emptyCart.setItems(new ArrayList<>());  // 空商品列表
            emptyCart.setTotalPrice(0.0);

            logger.info("Returning empty cart for customerId={}", customerId);
            return new ResponseEntity<>(emptyCart, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching cart for customerId={}: {}", customerId, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // API 用于更新购物车中商品的数量
    @PostMapping("/updateQuantity")
    public ResponseEntity<String> updateProductQuantity(
            @RequestParam(value = "customerId", defaultValue = DEFAULT_USER_ID) String customerId,
            @RequestParam String productId,
            @RequestParam int quantity) {

        logger.info("Request to update product quantity: customerId={}, productId={}, quantity={}", customerId, productId, quantity);

        try {
            cartService.updateProductQuantity(customerId, productId, quantity);
            logger.info("Quantity updated successfully for customerId={}, productId={}", customerId, productId);
            return new ResponseEntity<>("Quantity updated successfully", HttpStatus.OK);
        } catch (CartNotFoundException e) {
            logger.warn("Cart not found for customerId={}: {}", customerId, e.getMessage());
            return new ResponseEntity<>("Cart not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error updating quantity for customerId={}: {}", customerId, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
