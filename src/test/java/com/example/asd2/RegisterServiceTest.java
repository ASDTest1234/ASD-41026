package com.example.asd2;

import com.example.asd2.Model.RegisterUser;
import com.example.asd2.repository.UserRepository;
import com.example.asd2.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterUser() {
        // Given
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

  
        when(userRepository.save(any(RegisterUser.class))).thenReturn(registerUser);
        RegisterUser result = userService.registerUser(registerUser);
        assertNotNull(result);
        assertEquals("John", result.getFName());
        assertEquals("johndoe@example.com", result.getEmail());

        verify(userRepository, times(1)).save(registerUser);
    }
}
