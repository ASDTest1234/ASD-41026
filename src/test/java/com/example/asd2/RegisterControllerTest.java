package com.example.asd2;

import com.example.asd2.Model.RegisterUser;
import com.example.asd2.Service.RegisterUserService;
import com.example.asd2.Controller.RegisterUserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class RegisterUserControllerTest {

    @Mock
    private RegisterUserService registerUserService;

    @InjectMocks
    private RegisterUserController registerUserController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUserWithAllFieldsFilled() {
        // Arrange
        RegisterUser user = new RegisterUser();
        user.setfName("John");
        user.setlName("Doe");
        user.setDob("1990-01-01");
        user.setEmail("test@example.com");
        user.setPhone("1234567890");
        user.setPassword("password");
        user.setState("State");
        user.setCity("City");
        user.setSuburb("Suburb");
        user.setPostcode("12345");
        user.setStreetName("Main Street");
        user.setStreetNumber("123");
        user.setUnitNumber("Unit 5");

        when(registerUserService.registerUser(user)).thenReturn(user);

        // Act
        ResponseEntity<String> response = registerUserController.registerUser(user);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());
    }
}
