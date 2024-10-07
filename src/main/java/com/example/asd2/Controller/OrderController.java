package com.example.asd2.Controller;

import com.example.asd2.Model.Cart;
import com.example.asd2.Service.CartNotFoundException;
import com.example.asd2.Service.CartService;
import com.example.asd2.Service.OrderService;
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

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @GetMapping("/payment")
    public String showPaymentPage(@RequestParam String customerId, Model model) {
        model.addAttribute("customerId", customerId);
        return "payment";
    }

    @PostMapping("/confirm")
    public String confirmOrder(@RequestParam String customerId,
                               @RequestParam String cardNumber,
                               @RequestParam String expiryDate,
                               @RequestParam String cvv,
                               @RequestParam String address,
                               Model model) {

        try {
            Cart cart = cartService.getCartByCustomerId(customerId);
            if (cart == null || cart.getItems().isEmpty()) {
                model.addAttribute("error", "No items in cart to complete order.");
                return "payment";
            }

            // Simulate payment processing with card details (here only logging)
            // Real application should have secure payment gateway integration
            System.out.println("Processing payment for card: " + cardNumber);

            orderService.createOrder(customerId, cart.getItems());

            cartService.clearCart(customerId);  // 清空购物车
            return "orderConfirmation";
        } catch (CartNotFoundException e) {
            model.addAttribute("error", "Cart not found.");
            return "payment";
        } catch (Exception e) {
            model.addAttribute("error", "Order completion failed: " + e.getMessage());
            return "payment";
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
