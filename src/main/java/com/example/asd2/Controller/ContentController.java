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


    @GetMapping("/user/home_user")
    //the RequestParam makes sure there is input set into the URL, so it's like "http://localhost:8080/user/home_user?keyword
    public String searchProducts(@RequestParam(value = "filter", required = false, defaultValue = "") String keyword, Model model) {
        //using the product Service to find the product by it's name
        List<Products> products = productService.getSpecificProductByName(keyword);
        System.out.println("products " + productService.getAllProducts());
        //storing stuff into the products so it can be accessed in the HTMl page by thymeleaf.
        model.addAttribute("products", products);
        return "home_user";// returning the HTML page. 
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
