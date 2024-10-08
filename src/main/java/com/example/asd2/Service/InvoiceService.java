package com.example.asd2.Service;

import com.example.asd2.Model.Invoice;
import com.example.asd2.Model.Order;
import com.example.asd2.repository.InvoiceRepository;
import com.example.asd2.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<Invoice> getInvoicesByUserId(String userId) {
        return invoiceRepository.findByUserId(userId);
    }

    public List<Order> getOrdersByInvoice(Invoice invoice) {
        return orderRepository.findByIdIn(invoice.getOrderList());
    }
}
