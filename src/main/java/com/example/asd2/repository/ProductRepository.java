package com.example.asd2.repository;

import com.example.asd2.Model.Products;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Products, String> {
    Optional<Products> findByProductName(String productName);

    @Query("{ 'productName': { $regex: ?0 }}")
    List<Products> findProductByName(String Keyword);
}



