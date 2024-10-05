package com.example.asd2;

import com.example.asd2.Model.Response;
import com.example.asd2.repository.ResponseRepository;
import com.example.asd2.service.ResponseService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ResponseServiceTest {

    @Mock
    private ResponseRepository responseRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private ResponseService responseService;

    @Autowired
    public ResponseServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllResponses() {
        // Create sample responses
        Response response1 = new Response("R1", "Response Body 1");
        Response response2 = new Response("R2", "Response Body 2");
        List<Response> responses = Arrays.asList(response1, response2);

        // Mock the repository call
        when(responseRepository.findAll()).thenReturn(responses);

        // Call the service method
        List<Response> result = responseService.allResponses();

        // Assert the result
        assertEquals(2, result.size());
        assertEquals("R1", result.get(0).getResponse_id());
        assertEquals("Response Body 1", result.get(0).getResponse());
        assertEquals("R2", result.get(1).getResponse_id());
        assertEquals("Response Body 2", result.get(1).getResponse());
    }
    @Test
    public void testGetResponseById() {
        // Create a sample response
        Response response = new Response("R1", "Response Body 1");

        // Mock the repository call
        when(responseRepository.findByResponseId("R1")).thenReturn(Optional.of(response));

        // Call the service method
        Response result = responseService.getResponseById("R1");

        // Assert the result
        assertEquals("R1", result.getResponse_id());
        assertEquals("Response Body 1", result.getResponse());
    }

    @Test
    public void testGetResponseByIdNotFound() {
        // Mock the repository call to return an empty Optional
        when(responseRepository.findByResponseId("R2")).thenReturn(Optional.empty());

        // Call the service and assert that it throws an exception
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            responseService.getResponseById("R2");
        });

        // Verify the exception message
        assertEquals("Response not found with response_id: R2", thrown.getMessage());
    }
    @Test
    public void testUpdateResponse() {
        // Create a sample existing response
        Response existingResponse = new Response("R1", "Old Response Body");

        // Create the updated response
        Response updatedResponse = new Response("R1", "Updated Response Body");

        // Mock the repository to return the existing response
        when(responseRepository.findByResponseId("R1")).thenReturn(Optional.of(existingResponse));
        // Mock the repository's save method to return the updated response
        when(responseRepository.save(any(Response.class))).thenReturn(updatedResponse);

        // Act
        Response result = responseService.updateResponse("R1", "Updated Response Body");

        // Assert
        assertEquals("R1", result.getResponse_id());
        assertEquals("Updated Response Body", result.getResponse());

        // Verify interactions
        verify(responseRepository).findByResponseId("R1");
        verify(responseRepository).save(existingResponse); // Ensure save was called with the updated response object
    }

    @Test
    public void testUpdateResponseNotFound() {
        // Mock the repository to return an empty Optional
        when(responseRepository.findByResponseId("R2")).thenReturn(Optional.empty());

        // Act and Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            responseService.updateResponse("R2", "Some new body");
        });

        assertEquals("Response not found with response_id: R2", exception.getMessage());

        // Verify that the get method was called
        verify(responseRepository).findByResponseId("R2");
    }
    @Test
    public void testDeleteResponse() {
        // Create a sample response
        Response response = new Response("R1", "Response Body 1");

        // Mock the repository call to find the response
        when(responseRepository.findByResponseId("R1")).thenReturn(java.util.Optional.of(response));

        // Mock the repository delete behavior
        doNothing().when(responseRepository).delete(any(Response.class));

        // Act
        responseService.deleteResponse("R1");

        // Verify that the repository's findByResponseId and delete methods were called
        verify(responseRepository).findByResponseId("R1");
        verify(responseRepository).delete(response);
    }

    @Test
    public void testDeleteResponseNotFound() {
        // Mock the repository to return an empty Optional when trying to find a non-existent response
        when(responseRepository.findByResponseId("R999")).thenReturn(java.util.Optional.empty());

        // Act and Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            responseService.deleteResponse("R999");
        });

        // Check the exception message
        assertEquals("Response not found with response_id: R999", exception.getMessage());

        // Verify that the findByResponseId method was called
        verify(responseRepository).findByResponseId("R999");
    }



}
