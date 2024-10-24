package com.example.asd2.Controller;

import com.example.asd2.Model.Products;
import com.example.asd2.Model.Users;
import com.example.asd2.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/staff/product")
public class StaffProductController {

    @Autowired
    private ProductService productService;

    //gets all products from database
    @GetMapping("/inventory")
    public String showInventory(Model model) {
        List<Products> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "inventory";
    }

    //loads add product page
    @GetMapping("/new")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Products());
        return "add_product";
    }

    //Validates input then redirects to inventory page and displays success message
    @PostMapping("/add")
    public String addProduct(@ModelAttribute("product") Products product, RedirectAttributes redirectAttributes, Model model) {
        if (product.getProduct_Id() == null) {
            model.addAttribute("errorProductId", "Product ID required");
            return "add_product";
        }
        if (product.getProductName() == null || product.getProductName().length() < 3 || product.getProductName().length() > 50) {
            model.addAttribute("errorProductName", "Product Name must be between 3 and 50 characters");
            return "add_product";
        }
        if (product.getProductDescription() == null) {
            model.addAttribute("errorProductDescription", "Product Description Required");
            return "add_product";
        }
        if (product.getProductStock() < 0) {
            model.addAttribute("errorProductStock", "Stock cannot be negative");
            return "add_product";
        }
        if (Integer.parseInt(product.getProduct_Id()) < 0) {
            model.addAttribute("errorProductPrice", "Price cannot be negative");
            return "add_product";
        }
        if (product.getProductType() == null) {
            model.addAttribute("errorProductType", "Product Type Required");
            return "add_product";
        }
        if (product.getAdminID() == null) {
            model.addAttribute("errorAdminID", "Admin ID Required");
            return "add_product";
        }
        productService.addProduct(product);
        redirectAttributes.addFlashAttribute("message", "Product Added Successfully");
        return "redirect:/staff/product/inventory";
    }

    //deletes product and displays success message
    //double checks before deletion
    @PostMapping("/deleteByName")
    public String deleteProductByName(@RequestParam String productName, RedirectAttributes redirectAttributes) {
        try {
            boolean deleted = productService.deleteProductByName(productName);
            if (deleted) {
                redirectAttributes.addFlashAttribute("message", "Product Deleted Successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Product not found");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting product");
        }
        return "redirect:/staff/product/inventory";
    }


}
