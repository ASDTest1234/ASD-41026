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

@Controller
public class ContentController {// controller class that

    @Autowired
    private ProductService productService;

    @GetMapping("/admin/home_admin")//Get Mapping means what the URL is to get into the HTMl
    public String handleAdmin(){
        return "home_admin";
    }// returns the HTML when URL is entered.

//    @GetMapping("/user/home_user")
//    public String handleUser(){
//        return "home_user";
//    }

    @RequestMapping("/user/home_user")
    public String listProducts(Model model){
        List<Products> product = productService.getAllProducts();
        System.out.println("products " + product);
        model.addAttribute("products", productService.getAllProducts());
        return "home_user";
    }

//    @GetMapping("/Search")
//    public List<Products> searchProducts(@RequestParam("keyword") String keyword){
//        return productService.getSpecificProductByName(keyword);
//    }

    @GetMapping("/Search")
    public String searchProducts(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword, Model model) {
        List<Products> products = productService.getSpecificProductByName(keyword);
        model.addAttribute("products", products);
        return "Search";  // The name of your Thymeleaf template
    }




    @GetMapping("/staff/home_staff")
    public String handleStaff(){
        return "home_staff";
    }

    @GetMapping("logout")
    public String handleLogout(){return "logout";}
    @GetMapping("/login")
    public String handleLogin(){
        return "login";
    }



}
