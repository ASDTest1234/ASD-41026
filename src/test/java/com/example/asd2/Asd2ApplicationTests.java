package com.example.asd2;

import com.example.asd2.Model.Ticket;
import com.example.asd2.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class Asd2ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Use this to mock the TicketService
    private TicketService ticketService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllTickets() throws Exception {
        // Create a sample ticket
        Ticket ticket = new Ticket("T123", "T123", "C123", "Test Issue", "Test Description", "10/05/2024", null);

        // Mock the service call
        when(ticketService.allTickets()).thenReturn(Collections.singletonList(ticket));

        // Perform the GET request to retrieve all tickets
        mockMvc.perform(get("/minh/tickets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ticketId").value("T123")) // Check the first ticket ID
                .andExpect(jsonPath("$[0].issue").value("Test Issue")); // Check the issue of the first ticket
    }
    @Test
    void testSearchTicket() throws Exception {
        // Create a sample ticket
        Ticket ticket = new Ticket("T123", "T123", "C123", "Test Issue", "Test Description", "10/05/2024", null);

        // Mock the service call
        when(ticketService.aTicket("T123")).thenReturn(Optional.of(ticket));

        // Perform the GET request to search for a specific ticket
        mockMvc.perform(get("/minh/tickets/T123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId").value("T123")) // Check the ticket ID
                .andExpect(jsonPath("$.issue").value("Test Issue")); // Check the issue of the retrieved ticket
    }

    @Test
    void testSearchTicketNotFound() throws Exception {
        // Mock the service call to return an empty Optional
        when(ticketService.aTicket("T456")).thenReturn(Optional.empty());

        // Perform the GET request to search for a non-existing ticket
        mockMvc.perform(get("/minh/tickets/T456")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Expect a 404 Not Found status
    }
}
