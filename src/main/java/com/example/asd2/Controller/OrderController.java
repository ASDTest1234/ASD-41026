package com.example.asd2.Controller;

import com.example.asd2.Service.OrderService;
import com.example.asd2.Service.CartService;
import com.example.asd2.Model.Cart;
import com.example.asd2.Service.CartNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        // 订单处理逻辑

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
}
