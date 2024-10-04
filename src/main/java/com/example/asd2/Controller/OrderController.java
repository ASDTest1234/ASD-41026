package com.example.asd2.Controller;

import com.example.asd2.Service.OrderService;
import com.example.asd2.Service.CartService;
import com.example.asd2.Model.Cart;
import com.example.asd2.Service.CartNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @PostMapping("/complete")
    public ResponseEntity<String> completeOrder(@RequestParam String customerId) {
        try {
            Cart cart = cartService.getCartByCustomerId(customerId);
            if (cart == null || cart.getItems().isEmpty()) {
                return new ResponseEntity<>("No items in cart to complete order.", HttpStatus.BAD_REQUEST);
            }

            // 传递 customerId 和 items 列表给 createOrder 方法
            orderService.createOrder(customerId, cart.getItems());

            // 清空购物车
            cartService.clearCart(customerId);

            return new ResponseEntity<>("Order completed successfully", HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Order completion failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
