package com.example.asd2.service;

import com.example.asd2.Model.Ticket;
import com.example.asd2.repository.TicketRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    public List<Ticket> allTickets(){
        return  ticketRepository.findAll();
    }

    public Optional<Ticket> aTicket(String ticket_id){
        return ticketRepository.findTicketByTicketId(ticket_id);
    }
}
