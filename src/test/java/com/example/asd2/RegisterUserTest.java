package com.example.asd2;

import com.example.asd2.Model.RegisterUser;
import com.example.asd2.Service.RegisterUserService;
import com.example.asd2.Controller.RegisterUserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterUserTest {

    @InjectMocks
    private RegisterUserController registerUserController;

    @Mock
    private RegisterUserService registerUserService;

    private RegisterUser registerUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        registerUser = new RegisterUser();
        registerUser.setFName("test");
        registerUser.setLName("test");
        registerUser.setEmail("test@test.com");
        registerUser.setPassword("test");
        registerUser.setPhone("0000000000");
        registerUser.setDob("1999-01-01");
        registerUser.setState("NSW");
        registerUser.setCity("Sydney");
        registerUser.setSuburb("Ultimo");
        registerUser.setPostcode("0000");
        registerUser.setStreetName("Broadway");
        registerUser.setStreetNumber("11");
        registerUser.setUnitNumber("101");
    }

    @Test
    void testRegisterUser() {
        // Given
        when(registerUserService.registerUser(any(RegisterUser.class))).thenReturn(registerUser);

        // When
        String result = registerUserController.registerUser(registerUser, mock(Model.class));

        // Then
        assertEquals("redirect:/success", result);
        verify(registerUserService, times(1)).registerUser(registerUser);
    }

    @Test
    void testFindByEmail_UserExists() {
        // Given
        when(registerUserService.findByEmail(registerUser.getEmail())).thenReturn(Optional.of(registerUser));

        // When
        Optional<RegisterUser> result = registerUserService.findByEmail(registerUser.getEmail());

        // Then
        assertTrue(result.isPresent());
        assertEquals("test", result.get().getFName());
        verify(registerUserService, times(1)).findByEmail(registerUser.getEmail());
    }

    @Test
    void testFindByEmail_UserDoesNotExist() {
        // Given
        when(registerUserService.findByEmail(registerUser.getEmail())).thenReturn(Optional.empty());

        // When
        Optional<RegisterUser> result = registerUserService.findByEmail(registerUser.getEmail());

        // Then
        assertFalse(result.isPresent());
        verify(registerUserService, times(1)).findByEmail(registerUser.getEmail());
    }
}
