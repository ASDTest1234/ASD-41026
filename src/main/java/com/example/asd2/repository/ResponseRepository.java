package com.example.asd2.repository;

import com.example.asd2.Model.Response;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ResponseRepository extends MongoRepository<Response, ObjectId> {
    @Query("{ 'response_id': ?0 }")
    Optional<Response> findByResponseId(String responseId);
}
