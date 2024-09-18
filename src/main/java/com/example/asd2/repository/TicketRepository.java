package com.example.asd2.repository;


import com.example.asd2.Model.Ticket;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, ObjectId> {

}
