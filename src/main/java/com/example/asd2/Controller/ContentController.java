package com.example.asd2.Controller;

import com.example.asd2.Model.Products;
import com.example.asd2.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class ContentController {

    @Autowired
    private ProductService productService;

    @GetMapping("/admin/home_admin")
    public String handleAdmin() {
        return "home_admin";
    }

    @RequestMapping("/user/home_user")
    public String listProducts(Model model) {
        // Retrieve all products to display in the user home page
        List<Products> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "home_user";
    }

    @GetMapping("/Search")
    public String searchProducts(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword, Model model) {
        Optional<Products> products = productService.getProductByName(keyword);
        model.addAttribute("products", products);
        return "Search";
    }

    @GetMapping("/staff/home_staff")
    public String handleStaff() {
        return "home_staff";
    }

    @GetMapping("/logout")
    public String handleLogout() {
        return "logout";
    }

    @GetMapping("/login")
    public String handleLogin() {
        return "login";
    }

    @GetMapping("/cart")
    public String handleCart() {
        // Renders the cart view without needing any parameters
        return "cart";
    }
}
