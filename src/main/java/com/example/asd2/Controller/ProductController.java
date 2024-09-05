package com.example.asd2.Controller;

import com.example.asd2.Model.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping("/DashBoard")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.listAllProducts());
        return "DashBoard";  // This will return the DashBoard.html template
    }
}
