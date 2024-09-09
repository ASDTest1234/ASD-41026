package com.example.asd2.service;
import com.example.asd2.Model.Ticket;

import java.util.List;

public interface TicketService {

    public List<Ticket> getTicket();

    public Ticket addTicket(Ticket ticket);

    public Ticket deleteTicket(String id);

    public Ticket updateTicket(String id, Ticket ticket);


}
