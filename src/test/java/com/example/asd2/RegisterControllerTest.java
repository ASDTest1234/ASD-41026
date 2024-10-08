package com.example.asd2;

import com.example.asd2.Model.RegisterUser;
import com.example.asd2.Service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegisterUserController.class)
class RegisterUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService; // Mocking the service layer

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRegisterUser() throws Exception {
      
        RegisterUser registerUser = new RegisterUser();
        registerUser.setFName("John");
        registerUser.setLName("Doe");
        registerUser.setDob("2000-01-01");
        registerUser.setEmail("johndoe@example.com");
        registerUser.setPhone("1234567890");
        registerUser.setPassword("password");
        registerUser.setState("New York");
        registerUser.setCity("New York City");
        registerUser.setSuburb("Manhattan");
        registerUser.setPostcode("10001");
        registerUser.setStreetName("5th Avenue");
        registerUser.setStreetNumber("1");
        registerUser.setUnitNumber("A");

        // Mock the service call
        Mockito.when(userService.registerUser(Mockito.any(RegisterUser.class)))
               .thenReturn(registerUser);

        
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerUser)))
                .andExpect(status().isOk());
        
       
    }
}
