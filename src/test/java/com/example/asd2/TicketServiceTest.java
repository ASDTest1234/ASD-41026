package com.example.asd2;

import com.example.asd2.Model.Response;
import com.example.asd2.Model.Ticket;
import com.example.asd2.repository.TicketRepository;
import com.example.asd2.service.TicketService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
class TicketServiceTest {

    @MockBean
    private TicketRepository ticketRepository; // Mock the TicketRepository

    @InjectMocks
    @Autowired
    private TicketService ticketService; // The service being tested

    @Test
    void testGetAllTickets() {
        // Arrange
        Ticket ticket1 = new Ticket("ID1", "TID1", "CIC1", "Test Issue", "Test Description", "10/05/2024", null);
        Ticket ticket2 = new Ticket("ID2", "TID2", "CIC1", "Another Issue", "Another Description", "10/05/2024", null);
        List<Ticket> tickets = List.of(ticket1, ticket2);

        // Mock the repository method to return the sample tickets
        when(ticketRepository.findAll()).thenReturn(tickets);

        // Act
        List<Ticket> result = ticketService.allTickets();

        // Assert
        assertEquals(2, result.size()); // Check the size of the returned list
        assertEquals("TID1", result.get(0).getTicketId()); // Verify the first ticket ID
        assertEquals("TID2", result.get(1).getTicketId()); // Verify the second ticket ID

        // Verify that the ticket repository's findAll method was called
        verify(ticketRepository).findAll();
    }
    @Test
    void testSearchATicket() {
        // Arrange
        Ticket ticket = new Ticket("ID1", "TID1", "CID1", "Test Issue", "Test Description", "10/05/2024", null);

        // Mock the repository method to return an Optional containing the ticket
        when(ticketRepository.findTicketByTicketId("TID1")).thenReturn(Optional.of(ticket));

        // Act
        Optional<Ticket> result = ticketService.aTicket("TID1");

        // Assert
        assertTrue(result.isPresent()); // Ensure that the returned Optional is present
        assertEquals("TID1", result.get().getTicketId()); // Verify the ticket ID is as expected

        // Verify that the repository method was called
        verify(ticketRepository).findTicketByTicketId("TID1");
    }

    // Test for searching a non-existing ticket
    @Test
    void testSearchATicketNotFound() {
        // Mock the repository method to return an empty Optional for a non-existing ticket
        when(ticketRepository.findTicketByTicketId("T456")).thenReturn(Optional.empty());

        // Act
        Optional<Ticket> result = ticketService.aTicket("T456");

        // Assert
        assertTrue(result.isEmpty()); // Ensure that the returned Optional is not present

        // Verify that the repository method was called
        verify(ticketRepository).findTicketByTicketId("T456");
    }
    @Test
    void testCreateTicket() {
        // Arrange
        Ticket ticket = new Ticket("ID1", "TID1", "CID1", "Test Issue", "Test Description", "10/05/2024", null);

        // Prepare the payload as a map
        Map<String, String> payload = new HashMap<>();
        payload.put("ticketId", "TID1");
        payload.put("customerId", "CID1");
        payload.put("issue", "Test Issue");
        payload.put("description", "Test Description");
        payload.put("date", "10/05/2024");

        // Mock the repository method to return the created ticket
        when(ticketRepository.insert(any(Ticket.class))).thenReturn(ticket);  // Update here

        // Act
        Ticket createdTicket = ticketService.createTicket(payload);

        // Assert
        assertEquals("TID1", createdTicket.getTicketId()); // Verify the ticket ID
        assertEquals("CID1", createdTicket.getCustomerId()); // Verify the customer ID
        assertEquals("Test Issue", createdTicket.getIssue()); // Verify the issue

        // Verify that the repository's insert method was called with the correct ticket
        verify(ticketRepository).insert(any(Ticket.class)); // Update this line as needed
    }
    @Test
    void testGetTicketsByCustomerId() {
        // Arrange
        Ticket ticket1 = new Ticket("ID1", "TID1", "CID1", "Test Issue", "Test Description", "10/05/2024", null);
        Ticket ticket2 = new Ticket("ID2", "TID2", "CID1", "Another Issue", "Another Description", "10/05/2024", null);

        // Prepare a list of tickets for the customer
        List<Ticket> tickets = List.of(ticket1, ticket2);

        // Mock the repository method to return the list of tickets for the customer ID "CID1"
        when(ticketRepository.findTicketsByCustomerId("CID1")).thenReturn(tickets);

        // Act
        List<Ticket> result = ticketService.myTickets("CID1");

        // Assert
        assertEquals(2, result.size()); // Check the size of the returned list
        assertEquals("TID1", result.get(0).getTicketId()); // Verify the first ticket ID
        assertEquals("TID2", result.get(1).getTicketId()); // Verify the second ticket ID

        // Verify that the repository method was called with the expected customer ID
        verify(ticketRepository).findTicketsByCustomerId("CID1");
    }

    @Test
    void testGetTicketsByCustomerIdNotFound() {
        // Mock the service call to return an empty list for the customer ID "CID456"
        when(ticketRepository.findTicketsByCustomerId("CID456")).thenReturn(List.of());

        // Act
        List<Ticket> result = ticketService.myTickets("CID456");

        // Assert
        assertEquals(0, result.size()); // Ensure that no tickets are returned for this customer ID

        // Verify that the repository method was called with the expected customer ID
        verify(ticketRepository).findTicketsByCustomerId("CID456");
    }
    @Test
    void testGetResponsesForTicket() {
        // Arrange
        Response response1 = new Response("R1", "Response to Issue 1");
        Response response2 = new Response("R2", "Response to Issue 2");

        List<Response> responses = new ArrayList<>();
        responses.add(response1);
        responses.add(response2);

        Ticket ticket = new Ticket("ID1", "TID1", "CID1", "Test Issue", "Test Description", "10/05/2024", responses);

        // Mock the repository method to return the ticket with responses
        when(ticketRepository.findTicketByTicketId("TID1")).thenReturn(Optional.of(ticket));

        // Act
        Optional<Ticket> result = ticketService.aTicket("TID1");

        // Assert
        assertTrue(result.isPresent()); // Ensure that the ticket is present
        assertEquals(2, result.get().getResponseIds().size()); // Check the size of responses
        assertEquals("R1", result.get().getResponseIds().get(0).getResponse_id()); // Check the first response ID
        assertEquals("R2", result.get().getResponseIds().get(1).getResponse_id()); // Check the second response ID

        // Verify that the repository method was called
        verify(ticketRepository).findTicketByTicketId("TID1");
    }

    @Test
    void testGetResponsesForTicketNotFound() {
        // Mock the repository method to return an empty Optional for a non-existing ticket
        when(ticketRepository.findTicketByTicketId("T456")).thenReturn(Optional.empty());

        // Act
        Optional<Ticket> result = ticketService.aTicket("T456");

        // Assert
        assertTrue(result.isEmpty()); // Ensure that no ticket is found

        // Verify that the repository method was called
        verify(ticketRepository).findTicketByTicketId("T456");
    }
    @Test
    void testUpdateTicket() {
        // Arrange
        Ticket existingTicket = new Ticket("ID1", "TID1", "CID1", "Test Issue", "Test Description", "10/05/2024", null);

        // Mock the repository method to return the existing ticket when looking it up
        when(ticketRepository.findTicketByTicketId("TID1")).thenReturn(Optional.of(existingTicket));

        // Prepare the updates as a map
        Map<String, String> updates = new HashMap<>();
        updates.put("issue", "Updated Issue");
        updates.put("description", "Updated Description");

        // Act
        Optional<Ticket> updatedTicket = ticketService.updateTicket("TID1", updates);

        // Assert
        assertTrue(updatedTicket.isPresent()); // Ensure that the updated ticket is present
        assertEquals("Updated Issue", updatedTicket.get().getIssue()); // Check updated issue
        assertEquals("Updated Description", updatedTicket.get().getDescription()); // Check updated description

        // Verify that the repository's save method was called with the updated ticket
        verify(ticketRepository).save(existingTicket);
    }

    @Test
    void testUpdateTicketNotFound() {
        // Prepare the updates as a map
        Map<String, String> updates = new HashMap<>();
        updates.put("issue", "Updated Issue");
        updates.put("description", "Updated Description");

        // Mock the repository method to return an empty Optional
        when(ticketRepository.findTicketByTicketId("TID456")).thenReturn(Optional.empty());

        // Act
        Optional<Ticket> updatedTicket = ticketService.updateTicket("TID456", updates);

        // Assert
        assertTrue(updatedTicket.isEmpty()); // Ensure that no updated ticket is returned

        // Verify that the repository's save method was not called
        verify(ticketRepository).findTicketByTicketId("TID456");
    }
    @Test
    void testDeleteTicket() {
        // Arrange
        Ticket ticket = new Ticket("ID1", "TID1", "CID1", "Test Issue", "Test Description", "10/05/2024", null);

        // Mock the repository method to return the ticket when looking it up
        when(ticketRepository.findTicketByTicketId("TID1")).thenReturn(Optional.of(ticket));

        // Act
        boolean isDeleted = ticketService.deleteTicket("TID1");

        // Assert
        assertTrue(isDeleted); // Ensure that the ticket was successfully deleted

        // Verify that the repository's delete method was called
        verify(ticketRepository).delete(ticket); // Ensure that delete was called once with the ticket
    }

    @Test
    void testDeleteTicketNotFound() {
        // Arrange
        String ticketId = "TID456";

        // Mock the repository method to return an empty Optional
        when(ticketRepository.findTicketByTicketId(ticketId)).thenReturn(Optional.empty());

        // Act
        boolean isDeleted = ticketService.deleteTicket(ticketId);

        // Assert
        assertTrue(!isDeleted); // Ensure that the ticket was not deleted

        // Verify that the repository's find method was called
        verify(ticketRepository).findTicketByTicketId(ticketId); // Ensure that find was called
    }



}
