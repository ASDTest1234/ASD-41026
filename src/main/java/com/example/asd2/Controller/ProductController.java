package com.example.asd2.Controller;

import com.example.asd2.Service.CartService;
import com.example.asd2.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @GetMapping("/viewProducts")
    public String viewProducts(Model model, Authentication authentication) {
        String userId = authentication.getName();  // Retrieve the user ID from the SecurityContext
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("userId", userId); // Pass the userId to the view
        return "userProductPage";
    }

    @PostMapping("/addToCart")
    @ResponseBody
    public String addToCart(@RequestParam String customerId, @RequestParam String productId, @RequestParam int quantity) {
        System.out.println("Received customerId: " + customerId + ", productId: " + productId + ", quantity: " + quantity);

        try {
            cartService.addToCart(customerId, productId, quantity);
            return "Product added to cart";
        } catch (Exception e) {
            return "Failed to add product to cart: " + e.getMessage();
        }
    }

}
