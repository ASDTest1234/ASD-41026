package com.example.asd2.Controller;

import com.example.asd2.Model.Invoice;
import com.example.asd2.Model.Order;
import com.example.asd2.Service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    // Display all invoices for a user
    @GetMapping("/{userId}")
    public String getInvoicesByUser(@PathVariable String userId, Model model) {
        List<Invoice> invoices = invoiceService.getInvoicesByUserId(userId);
        model.addAttribute("invoices", invoices);
        return "invoices";
    }

    // Display details for a specific order
    @GetMapping("/order/{invoiceId}")
    public String getOrderDetails(@PathVariable String invoiceId, Model model) {
        Invoice invoice = invoiceService.getInvoicesByUserId(invoiceId).get(0);
        List<Order> orders = invoiceService.getOrdersByInvoice(invoice);
        model.addAttribute("orders", orders);
        return "orderDetails";
    }
}