package com.example.asd2.Controller;

import com.example.asd2.Model.Cart;
import com.example.asd2.Service.CartService;
import com.example.asd2.Service.CartNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;


    @GetMapping("/customer123")
    public ResponseEntity<Cart> getCartForCustomer123() {
        try {
            Cart cart = cartService.getCartByCustomerId("customer123");
            return new ResponseEntity<>(cart, HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
