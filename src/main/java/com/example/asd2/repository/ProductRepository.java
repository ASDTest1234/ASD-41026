package com.example.asd2.repository;

import com.example.asd2.Model.Products;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Products, String> {
    Optional<Products> findByProductName(String productName); // 根据 productName 查询
}