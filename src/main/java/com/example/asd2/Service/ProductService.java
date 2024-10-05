package com.example.asd2.Service;

import com.example.asd2.Model.Products;
import com.example.asd2.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public Optional<Products> getProductByName(String name){
        return productRepository.findByProductName(name);
    }

    public List<Products> getAllProducts(){
        return productRepository.findAll();
    }

    public List<Products> getSpecificProductByName(String keyword){
        return productRepository.findProducyByName(keyword);
    }
}
