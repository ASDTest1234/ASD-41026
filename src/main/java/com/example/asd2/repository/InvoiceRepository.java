package com.example.asd2.repository;

import com.example.asd2.Model.Invoice;
import com.example.asd2.Model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface InvoiceRepository extends MongoRepository<Invoice, String> {
    List<Invoice> findByUserId(String userId);
}

