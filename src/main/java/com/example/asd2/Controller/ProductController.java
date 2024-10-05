package com.example.asd2.Controller;

import com.example.asd2.Service.CartService;
import com.example.asd2.Service.ProductService;
import com.example.asd2.Model.Products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @GetMapping("/{ProductName}")
    public ResponseEntity<Optional<Products>> searchProductName(@PathVariable String ProductName){
        return new ResponseEntity<>(productService.getProductByName(ProductName), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Products>> getAllProducts(){
        List<Products> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // 添加到购物车的端点
    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCart(
            @RequestParam String customerId,
            @RequestParam String productId,
            @RequestParam int quantity) {
        try {
            cartService.addToCart(customerId, productId, quantity);
            return new ResponseEntity<>("Product added to cart", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add product to cart: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
