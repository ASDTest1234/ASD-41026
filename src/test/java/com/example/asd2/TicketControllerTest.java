package com.example.asd2;

import com.example.asd2.Model.Response;
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


import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TicketControllerTest {

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
        verify(ticketService).allTickets();
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
        verify(ticketService).aTicket("T123");
    }

    @Test
    void testSearchTicketNotFound() throws Exception {
        // Mock the service call to return an empty Optional
        when(ticketService.aTicket("T456")).thenReturn(Optional.empty());

        // Perform the GET request to search for a non-existing ticket
        mockMvc.perform(get("/minh/tickets/T456")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Expect a 404 Not Found status
        verify(ticketService).aTicket("T456");
    }
    @Test
    void testCreateTicket() throws Exception {
        // Create a sample ticket
        Ticket ticket = new Ticket("T123", "T123", "C123", "Test Issue", "Test Description", "10/05/2024", null);

        // Prepare the payload as a map
        Map<String, String> payload = new HashMap<>();
        payload.put("ticketId", "T123");
        payload.put("customerId", "C123");
        payload.put("issue", "Test Issue");
        payload.put("description", "Test Description");
        payload.put("date", "10/05/2024");

        // Mock the service call to return the created ticket
        when(ticketService.createTicket(payload)).thenReturn(ticket);

        // Perform the POST request to create a new ticket
        mockMvc.perform(post("/minh/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))) // Convert the Map to JSON
                .andExpect(status().isCreated()) // Expect a 201 Created status
                .andExpect(jsonPath("$.ticketId").value("T123")) // Check the ticket ID
                .andExpect(jsonPath("$.issue").value("Test Issue")); // Check the issue of the created ticket
        verify(ticketService).createTicket(payload);
    }
    @Test
    void testUpdateTicket() throws Exception {
        // Create a sample ticket before the update
        Ticket existingTicket = new Ticket("T123", "T123", "C123", "Test Issue", "Test Description", "10/05/2024", null);

        // Create a ticket after the update
        Ticket updatedTicket = new Ticket("T123", "T123", "C123", "Updated Issue", "Updated Description", "10/05/2024", null);

        // Prepare the updates as a map
        Map<String, String> updates = new HashMap<>();
        updates.put("issue", "Updated Issue");
        updates.put("description", "Updated Description");

        // Mock the service call to return the existing ticket when looking it up
        when(ticketService.aTicket("T123")).thenReturn(Optional.of(existingTicket));

        // Mock the service call to return the updated ticket when it's updated
        when(ticketService.updateTicket("T123", updates)).thenReturn(Optional.of(updatedTicket));

        // Perform the PUT request to update the existing ticket
        mockMvc.perform(put("/minh/tickets/T123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates))) // Convert the updates to JSON
                .andExpect(status().isOk()) // Expect a 200 OK status
                .andExpect(jsonPath("$.ticketId").value("T123")) // Check the ticket ID
                .andExpect(jsonPath("$.issue").value("Updated Issue")) // Check updated issue
                .andExpect(jsonPath("$.description").value("Updated Description")); // Check updated description
    }

    @Test
    void testUpdateTicketNotFound() throws Exception {
        // Prepare the updates as a map
        Map<String, String> updates = new HashMap<>();
        updates.put("issue", "Updated Issue");
        updates.put("description", "Updated Description");

        // Mock the service call to return an empty Optional
        when(ticketService.aTicket("T456")).thenReturn(Optional.empty());

        // Perform the PUT request to update a non-existing ticket
        mockMvc.perform(put("/minh/tickets/T456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates))) // Convert the updates to JSON
                .andExpect(status().isNotFound()); // Expect a 404 Not Found status


    }


    @Test
    void testDeleteTicket() throws Exception {
        // Mocking ticket ID that will be deleted
        String ticketId = "T123";

        // Mock the service call to return true (indicating successful deletion)
        when(ticketService.deleteTicket(ticketId)).thenReturn(true);

        // Perform the DELETE request
        mockMvc.perform(delete("/minh/tickets/" + ticketId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // Expect a 204 No Content status

        // Verify that the service's delete method was called once with the ticketId
        verify(ticketService).deleteTicket(ticketId);
    }

    @Test
    void testDeleteTicketNotFound() throws Exception {
        // Mocking ticket ID that does not exist
        String ticketId = "T456";

        // Mock the service call to return false (indicating failure to delete)
        when(ticketService.deleteTicket(ticketId)).thenReturn(false);

        // Perform the DELETE request
        mockMvc.perform(delete("/minh/tickets/" + ticketId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Expect a 404 Not Found status
        verify(ticketService).deleteTicket(ticketId);
    }
    @Test
    void testGetTicketsByCustomerId() throws Exception {
        // Create a sample ticket
        Ticket ticket1 = new Ticket("T123", "T123", "C123", "Test Issue", "Test Description", "10/05/2024", null);
        Ticket ticket2 = new Ticket("T124", "T124", "C123", "Another Issue", "Another Description", "10/05/2024", null);

        // Prepare a list of tickets for the customer
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket1);
        tickets.add(ticket2);

        // Mock the service call to return the list of tickets for the customer ID "C123"
        when(ticketService.myTickets("C123")).thenReturn(tickets);

        // Perform the GET request for tickets by customer ID
        mockMvc.perform(get("/minh/tickets/mytickets/C123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expect 200 OK status
                .andExpect(jsonPath("$[0].ticketId").value("T123")) // Check the first ticket ID
                .andExpect(jsonPath("$[1].ticketId").value("T124")); // Check the second ticket ID
        verify(ticketService).myTickets("C123");
    }

    @Test
    void testGetTicketsByCustomerIdNotFound() throws Exception {
        // Mock the service call to return an empty list for the customer ID "C456"
        when(ticketService.myTickets("C456")).thenReturn(new ArrayList<>());

        // Perform the GET request for tickets by customer ID
        mockMvc.perform(get("/minh/tickets/mytickets/C456")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Expect a 404 Not Found status
        verify(ticketService).myTickets("C456");
    }
    @Test
    void testGetResponsesForTicket() throws Exception {
        // Create a sample response with expected fields
        Response response1 = new Response("R1", "Response to Issue 1");
        Response response2 = new Response("R2", "Response to Issue 2");

        List<Response> responses = new ArrayList<>();
        responses.add(response1);
        responses.add(response2);

        // Create a sample ticket with responses
        Ticket ticket = new Ticket("T123", "T123", "C123", "Test Issue", "Test Description", "10/05/2024", responses);

        // Mock the service call to return the ticket with responses
        when(ticketService.aTicket("T123")).thenReturn(Optional.of(ticket));

        // Perform the GET request to retrieve responses for the ticket
        mockMvc.perform(get("/minh/tickets/T123/responses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expect 200 OK status
                .andExpect(jsonPath("$[0].response_id").value("R1")) // Check the first response ID
                .andExpect(jsonPath("$[1].response_id").value("R2")); // Check the second response ID
        verify(ticketService).aTicket("T123");
    }


    @Test
    void testGetResponsesForTicketNotFound() throws Exception {
        // Mock the service call to return an empty Optional
        when(ticketService.aTicket("T456")).thenReturn(Optional.empty());

        // Perform the GET request to retrieve responses for a non-existing ticket
        mockMvc.perform(get("/minh/tickets/T456/responses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Expect a 404 Not Found status
        verify(ticketService).aTicket("T456");
    }
}
