package com.example.asd2;

import com.example.asd2.Model.Response;
import com.example.asd2.service.ResponseService;
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
public class ResponseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Use this to mock the ResponseService
    private ResponseService responseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllResponses() throws Exception {
        // Create sample responses
        Response response1 = new Response("R1", "Response Body 1");
        Response response2 = new Response("R2", "Response Body 2");
        List<Response> responses = Arrays.asList(response1, response2);

        // Mock the service call
        when(responseService.allResponses()).thenReturn(responses);

        // Perform the GET request to retrieve all responses
        mockMvc.perform(get("/minh/response/allresponses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].response_id").value("R1")) // Check the first response ID
                .andExpect(jsonPath("$[0].response").value("Response Body 1")) // Check body of first response
                .andExpect(jsonPath("$[1].response_id").value("R2")) // Check the second response ID
                .andExpect(jsonPath("$[1].response").value("Response Body 2")); // Check body of second response

        verify(responseService).allResponses();
    }

    @Test
    public void testGetResponse() throws Exception {
        // Create a sample response
        Response response = new Response("R1", "Response Body 1");

        // Mock the service call
        when(responseService.getResponseById("R1")).thenReturn(response);

        // Perform the GET request to retrieve a specific response
        mockMvc.perform(get("/minh/response/R1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response_id").value("R1")) // Check response ID
                .andExpect(jsonPath("$.response").value("Response Body 1")); // Check response body

        verify(responseService).getResponseById("R1");
    }

    @Test
    public void testGetResponseNotFound() throws Exception {
        // Mock the service call to throw an exception for not found
        when(responseService.getResponseById("R3")).thenThrow(new RuntimeException("Response not found"));

        // Perform the GET request for a non-existing response
        mockMvc.perform(get("/minh/response/R3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Expect 404 Not Found

        verify(responseService).getResponseById("R3");
    }
    @Test
    public void testCreateResponse() throws Exception {
        // Create a sample response
        Response response = new Response("R1", "Response Body 1");

        // Prepare the payload as a map
        Map<String, String> payload = new HashMap<>();
        payload.put("response_id", "R1");
        payload.put("responseBody", "Response Body 1");
        payload.put("ticket_id", "T123");

        // Mock the service call to return the created response
        when(responseService.createResponse("R1", "Response Body 1", "T123")).thenReturn(response);

        // Perform the POST request to create a new response
        mockMvc.perform(post("/minh/response")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))) // Convert the Map to JSON
                .andExpect(status().isCreated()) // Expect a 201 Created status
                .andExpect(jsonPath("$.response_id").value("R1")) // Check the response ID
                .andExpect(jsonPath("$.response").value("Response Body 1")); // Check the response body

        // Verify that the service's createResponse method was called with the correct arguments
        verify(responseService).createResponse("R1", "Response Body 1", "T123");
    }
    @Test
    public void testUpdateResponse() throws Exception {
        // Create a sample existing response before the update
        Response existingResponse = new Response("R1", "Old Response Body");

        // Create a sample response after the update
        Response updatedResponse = new Response("R1", "Updated Response Body");

        // Prepare the updates as a map
        Map<String, String> updates = new HashMap<>();
        updates.put("response", "Updated Response Body");

        // Mock the service call to return the existing response when looking it up
        when(responseService.getResponseById("R1")).thenReturn(existingResponse);

        // Mock the service call to return the updated response when it's updated
        when(responseService.updateResponse("R1", "Updated Response Body")).thenReturn(updatedResponse);

        // Perform the PUT request to update the existing response
        mockMvc.perform(put("/minh/response/update/R1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates))) // Convert the updates to JSON
                .andExpect(status().isOk()) // Expect a 200 OK status
                .andExpect(jsonPath("$.response_id").value("R1")) // Check the response ID
                .andExpect(jsonPath("$.response").value("Updated Response Body")); // Check the updated response body

        // Verify that the service's updateResponse method was called with the correct arguments
        verify(responseService).updateResponse("R1", "Updated Response Body");
    }
    @Test
    public void testDeleteResponse() throws Exception {
        // Perform the DELETE request to delete an existing response
        mockMvc.perform(delete("/minh/response/delete/R1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // Expect a 204 No Content status

        // Verify that the service's deleteResponse method was called
        verify(responseService).deleteResponse("R1"); // Just verify, no return is expected
    }


}

