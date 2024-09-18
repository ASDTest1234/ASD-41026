package com.example.asd2.repository;

import com.example.asd2.Model.Response;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResponseRepository extends MongoRepository<Response, ObjectId> {

}
