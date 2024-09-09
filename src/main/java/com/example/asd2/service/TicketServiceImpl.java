package com.example.asd2.service;
import com.example.asd2.Model.Ticket;
import com.example.asd2.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService{

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public List<Ticket> getTicket() {
        return ticketRepository.findAll();
    }

    @Override
    public Ticket addTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket deleteTicket(String id) {
        Ticket ticket = ticketRepository.findById(id).get();
        ticketRepository.delete(ticket);
        return ticket;
    }

    @Override
    public Ticket updateTicket(String id, Ticket ticket) {
        Ticket ticket2 = ticketRepository.findById(id).get();
        ticket2.setTicketId(ticket.getTicketId());
        ticket2.setCustomerId(ticket.getCustomerId());
        ticket2.setIssue(ticket.getIssue());
        ticket2.setDescription(ticket.getDescription());
        ticketRepository.save(ticket2);
        return ticket2;
    }


}
