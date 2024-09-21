package com.example.asd2.Controller;
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
    public ResponseEntity<Optional<Ticket>> searchATicket(@PathVariable String ticket_id){
        return new ResponseEntity<Optional<Ticket>>(ticketService.aTicket(ticket_id), HttpStatus.OK);
    }

}
