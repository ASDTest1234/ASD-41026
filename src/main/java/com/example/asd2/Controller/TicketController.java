package com.example.asd2.Controller;


import com.example.asd2.Model.Ticket;
import com.example.asd2.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;
    @GetMapping("/all")
    public List<Ticket> getTickets(){
        return ticketService.getTicket();
    }
    @PostMapping("/insert")
    public Ticket insert(@RequestBody Ticket ticket){
        return ticketService.addTicket(ticket);
    }
    @PutMapping("/update/{id}")
    public  Ticket update(@PathVariable String id, @RequestBody Ticket ticket ){
        return ticketService.updateTicket(id, ticket);
    }
    @DeleteMapping("/delete/{id}")
    public Ticket delete(@PathVariable String id){
        return ticketService.deleteTicket(id);
    }
}
