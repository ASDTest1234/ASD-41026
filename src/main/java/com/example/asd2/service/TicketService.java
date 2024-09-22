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

    public List<Ticket> myTickets(String customerId) {
        return ticketRepository.findTicketsByCustomerId(customerId);
    }

    public Optional<Ticket> updateTicket(String ticketId, Map<String, String> updates) {
        Optional<Ticket> optionalTicket = ticketRepository.findTicketByTicketId(ticketId);

        if (optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();

            // Update fields
            //if (updates.containsKey("customerId")) {
            //    ticket.setCustomerId(updates.get("customerId"));
            //}
            if (updates.containsKey("issue")) {
                ticket.setIssue(updates.get("issue"));
            }
            if (updates.containsKey("description")) {
                ticket.setDescription(updates.get("description"));
            }
            //if (updates.containsKey("date")) {
            //    ticket.setDate(updates.get("date"));
            //}

            // Save the updated ticket
            ticketRepository.save(ticket);
            return Optional.of(ticket);
        }
        return Optional.empty();
    }

    public boolean deleteTicket(String ticketId) {
        Optional<Ticket> optionalTicket = ticketRepository.findTicketByTicketId(ticketId);
        if (optionalTicket.isPresent()) {
            ticketRepository.delete(optionalTicket.get());
            return true;
        }
        return false;
    }
}
