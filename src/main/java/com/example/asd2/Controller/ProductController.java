package com.example.asd2.Controller;

import com.example.asd2.Model.Products;
import com.example.asd2.Model.Users;
import com.example.asd2.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/{ProductName}")
    public ResponseEntity<Optional<Products>> searchProductName(@PathVariable String ProductName){
//        return new ResponseEntity<Optional<Users>>(Optional.ofNullable(userService.getUserByID(ID)), HttpStatus.OK);
        return new ResponseEntity<Optional<Products>>(productService.getProductByName(ProductName), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Products>> getAllProducts(){
        List<Products> product = productService.getAllProducts();  // Fetch all users from the database
        return new ResponseEntity<>(product, HttpStatus.OK);  // Return the users in the response
    }

    @RequestMapping("/products")
    public String listProducts(Model model){
        List<Products> product = productService.getAllProducts();
        System.out.println("products " + product);
        model.addAttribute("products", productService.getAllProducts());
        return "UserProductPage";
    }
}
