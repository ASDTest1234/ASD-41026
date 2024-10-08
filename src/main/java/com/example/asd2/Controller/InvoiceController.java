package com.example.asd2.Controller;

import com.example.asd2.Model.Cart;
import com.example.asd2.Model.Order;
import com.example.asd2.Service.CartNotFoundException;
import com.example.asd2.Service.CartService;
import com.example.asd2.Service.OrderService;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
@RequestMapping("/user/invoices")
public class InvoiceController {

    @Autowired
    private OrderService orderService;

    private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    @GetMapping("/list")
    public String listOrders(@RequestParam String customerId, Model model) {
        List<Order> orders = orderService.getOrdersByCustomerId(customerId);
        if (orders.isEmpty()) {
            logger.info("No orders found for customerId: " + customerId);
        } else {
            logger.info("Orders found for customerId: " + customerId + ": " + orders.size());
        }
        model.addAttribute("orders", orders);
        return "invoices";
    }

    @GetMapping("/invoiceDetails")
    public String orderDetails(@RequestParam String orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "invoiceDetails";
    }
}