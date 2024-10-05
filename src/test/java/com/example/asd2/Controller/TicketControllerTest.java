package com.example.asd2.Controller;

import com.example.asd2.Model.Ticket;
import com.example.asd2.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TicketController.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateTicket() throws Exception {
        // Prepare request payload
        Map<String, String> payload = new HashMap<>();
        payload.put("ticketId", "T123");
        payload.put("customerId", "C123");
        payload.put("issue", "Test Issue");
        payload.put("description", "Test Description");
        payload.put("date", "10/05/2024");

        // Create a Ticket object as a mock return value
        Ticket mockTicket = new Ticket("T123","T123", "C123", "Test Issue", "Test Description", "10/05/2024", null);

        // Mock the behavior of the ticketService
        when(ticketService.createTicket(any(Map.class))).thenReturn(mockTicket);

        // Perform the post request
        mockMvc.perform(post("/minh/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated());
    }
}

