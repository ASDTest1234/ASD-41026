package com.example.asd2.Model;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService{

    List<Product> listAllProducts();
}
