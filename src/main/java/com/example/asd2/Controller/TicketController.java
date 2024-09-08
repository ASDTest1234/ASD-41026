package com.example.asd2.Controller;

import com.example.asd2.Model.Ticket;
import com.example.asd2.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;  // Directly using repository

    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll(); // Fetch all tickets directly using the repository
    }

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        Ticket createdTicket = ticketRepository.save(ticket);
        return ResponseEntity.ok(createdTicket);
    }
}
