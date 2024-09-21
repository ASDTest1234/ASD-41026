package com.example.asd2.service;

import com.example.asd2.Model.Ticket;
import com.example.asd2.repository.TicketRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    public Ticket createTicket(Map<String, String> payload) {
        // Extract data from the payload Map
        String ticketId = payload.get("ticketId");
        String customerId = payload.get("customerId");
        String issue = payload.get("issue");
        String description = payload.get("description");
        String date = payload.get("date");

        // Create a new Ticket object
        Ticket ticket = new Ticket();
        ticket.setTicketId(ticketId);
        ticket.setCustomerId(customerId);
        ticket.setIssue(issue);
        ticket.setDescription(description);
        ticket.setDate(date);

        Ticket newTicket = ticketRepository.insert(ticket);
        return newTicket;
    }
    public List<Ticket> allTickets(){
        return  ticketRepository.findAll();
    }

    public Optional<Ticket> aTicket(String ticket_id){
        return ticketRepository.findTicketByTicketId(ticket_id);
    }
}
