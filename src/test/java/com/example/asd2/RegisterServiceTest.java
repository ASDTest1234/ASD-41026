package com.example.asd2;

import com.example.asd2.Model.RegisterUser;
import com.example.asd2.Service.RegisterUserService;
import com.example.asd2.repository.RegisterUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RegisterUserServiceTest {

    @Mock
    private RegisterUserRepository registerUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterUserService registerUserService;

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

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(registerUserRepository.save(any(RegisterUser.class))).thenReturn(user);

        // Act
        RegisterUser result = registerUserService.registerUser(user);

        // Assert
        assertEquals("encodedPassword", result.getPassword());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("John", result.getfName());
        assertEquals("Doe", result.getlName());
        assertEquals("1990-01-01", result.getDob());
        assertEquals("1234567890", result.getPhone());
        assertEquals("State", result.getState());
        assertEquals("City", result.getCity());
        assertEquals("Suburb", result.getSuburb());
        assertEquals("12345", result.getPostcode());
        assertEquals("Main Street", result.getStreetName());
        assertEquals("123", result.getStreetNumber());
        assertEquals("Unit 5", result.getUnitNumber());
    }
}
