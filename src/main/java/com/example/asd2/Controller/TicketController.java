package com.example.asd2.Controller;
import com.example.asd2.Model.Response;
import com.example.asd2.Model.Ticket;
import com.example.asd2.service.TicketService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("minh/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;
    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody Map<String, String> payload) {
        return new ResponseEntity<Ticket>(ticketService.createTicket(payload), HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets(){
        return new ResponseEntity<List<Ticket>>(ticketService.allTickets(), HttpStatus.OK);
    }
    @GetMapping("/{ticket_id}")
    public ResponseEntity<Ticket> searchATicket(@PathVariable String ticket_id) {
        Optional<Ticket> ticket = ticketService.aTicket(ticket_id);
        return ticket.map(ResponseEntity::ok) // if present, return it with a 200 status
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // return 404 if not found
    }

    @GetMapping("/mytickets/{customer_id}")
    public ResponseEntity<List<Ticket>> getTicketsByCustomerId(@PathVariable("customer_id") String customerId) {
        List<Ticket> tickets = ticketService.myTickets(customerId);
        if (tickets.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }
    @GetMapping("/{ticket_id}/responses")
    public ResponseEntity<List<Response>> getResponsesForTicket(@PathVariable String ticket_id) {
        return ticketService.aTicket(ticket_id)
                .map(ticket -> {
                    List<Response> responses = ticket.getResponseIds(); // Direct access
                    return new ResponseEntity<>(responses, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PutMapping("/{ticket_id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable String ticket_id, @RequestBody Map<String, String> updates) {
        Optional<Ticket> updatedTicket = ticketService.updateTicket(ticket_id, updates);
        return updatedTicket.map(ticket -> new ResponseEntity<>(ticket, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @DeleteMapping("/{ticket_id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable String ticket_id) {
        boolean isDeleted = ticketService.deleteTicket(ticket_id);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
