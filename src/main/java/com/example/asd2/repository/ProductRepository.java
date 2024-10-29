package com.example.asd2.repository;

import com.example.asd2.Model.Products;
import com.example.asd2.Model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Products, String> {
    // find the product by their name.
    Optional<Products> findByProductName(String productName);

    // does a MongoDB Query to filter the results based on the Keyword.
    @Query("{ 'productName': { $regex: ?0 }}")
    Page<Products> findProductByName(String Keyword, Pageable pageable);
}



