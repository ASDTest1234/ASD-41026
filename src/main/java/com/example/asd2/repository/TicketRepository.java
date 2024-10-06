package com.example.asd2.repository;


import com.example.asd2.Model.Ticket;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, ObjectId> {
    Optional<Ticket> findTicketByTicketId(String ticket_id);
    List<Ticket> findTicketsByCustomerId(String customerId);
}
