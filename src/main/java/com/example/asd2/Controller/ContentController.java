package com.example.asd2.Controller;

import com.example.asd2.Model.Products;
import com.example.asd2.Model.Users;
import com.example.asd2.Service.ProductService;
import com.example.asd2.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Controller
public class ContentController {

    @Autowired
    private ProductService productService;

    private UserService userService;

    public ContentController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping("/admin/home_admin")
//    public String handleAdmin() {
//        return "home_admin";
//    }

//    @RequestMapping("/admin/home_admin")
//    public String listUsers(Model model) {
//        // Retrieve all products to display in the user home page
//        List<Users> users = userService.getAllUsers();
//        model.addAttribute("users", users );
//        return "home_admin";
//    }

    @GetMapping("/admin/home_admin")
    //the RequestParam makes sure there is input set into the URL, so it's like "http://localhost:8080/user/home_user?keyword="
    public String searchUserRoles(@RequestParam(value = "filter", required = false, defaultValue = "") String keyword, Model model) {
        //using the product Service to find the product by its name
        List<Users> users = userService.getUsersByRoles(keyword);

        //storing stuff into the products so it can be accessed in the HTMl page by thymeleaf.
        model.addAttribute("users", users);
        return "home_admin";// returning the HTML page.
    }


//    @GetMapping("/user/home_user")
//    //the RequestParam makes sure there is input set into the URL, so it's like "http://localhost:8080/user/home_user?keyword="
//    public String searchProducts(@RequestParam(value = "filter", required = false, defaultValue = "") String keyword, Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value ="size" , defaultValue = "10") int size) {
//        //using the product Service to find the product by its name
//        List<Products> products = productService.getSpecificProductByName(keyword);
//        System.out.println("products " + productService.getAllProducts());
//        //storing stuff into the products so it can be accessed in the HTMl page by thymeleaf.
//        model.addAttribute("products", products);
//        return "home_user";// returning the HTML page.
//    }

    @GetMapping("/user/home_user")
    public String getProducts(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,// Default sort field
            @RequestParam(defaultValue = "asc") String sortDirection,     // Default sort direction,
            Model model) {

        // Fetch paginated products based on the filter
        Page<Products> productsPage = productService.getSpecificProductByName(filter, page,size, sortDirection);

        // Add products and pagination details to the model
        model.addAttribute("products", productsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productsPage.getTotalPages());
        model.addAttribute("keyword", filter);
        model.addAttribute("productsPage", productsPage);
        model.addAttribute("sortDirection", sortDirection);


        return "home_user"; // Replace with your actual template name
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

    @GetMapping("/user/cart")
    public String handleCart() {
        return "cart";
    }
    @GetMapping("/details")
    public String handleDetails() {
        return "details";
    }
    @GetMapping("/user/update_details")
    public String handleUpdateDetails() {
        return "update_details"; // Ensure this matches the name of your HTML file without extension
    }

    @GetMapping("/user/orders")
    public String handleOrders() {
        return "user_orders";
    }

    @GetMapping("/stuff_orders")
    public String handleStuffOrders() {
        return "stuff_orders";  // This returns the stuff_orders.html page
    }

    @GetMapping("/stuff_orders/stuff_orderDetails")
    public String handleStuffOrderDetails() {
        return "stuff_orderDetails";  // This returns the stuff_orderDetails.html page
    }

}
