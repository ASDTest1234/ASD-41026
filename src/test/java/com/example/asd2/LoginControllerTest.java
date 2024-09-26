package com.example.asd2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

//@SpringBootTest
//@AutoConfigureMockMvc
//public class LoginControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void givenValidCredentials_whenLogin_thenReturn200AndRedirect() throws Exception {
//        // Define valid credentials
//        String validUsername = "bob@gmail.com";
//        String validPassword = "123";
//
//        // Perform login request
//        mockMvc.perform(post("/login")
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                        .param("username", validUsername)
//                        .param("password", validPassword))
//                .andExpect(status().isOk()) // Expect HTTP 200
//                .andExpect(redirectedUrl("/staff/home_staff"));  // Expect redirection to /home or any valid URL
//    }
//}



//@SpringBootTest
//@AutoConfigureMockMvc
//public class LoginControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void testValidLogin() throws Exception {
//        mockMvc.perform(post("/login")
//                        .param("email", "bob@gmail.com")
//                        .param("password", "123"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/staff/home_staff"));
//    }
//}

import com.example.asd2.Model.Users;
import com.example.asd2.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository; // MongoDB user repository

    @Autowired
    private PasswordEncoder passwordEncoder; // Injected BCryptPasswordEncoder

//    @BeforeEach
//    public void setUp() {
//        // Clean the database before each test (optional)
//        //userRepository.deleteAll();
//
//        // Create and save the user with an encoded password
//        Users user = new Users();
//        user.setEmail("John@gmail.com");
//        user.setId("1");
//        user.setUsername("bob");
//        user.setPassword(passwordEncoder.encode("123")); // Encode the password using BCrypt
//        user.setRole("STAFF");
//
//        userRepository.save(user); // Save user to MongoDB
//    }

    @Test
    public void testValidLogin() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "test@gmail.com")
                        .param("password", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/staff/home_staff"));
    }

    @Test
    public void InvalidLogin() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "wrong")
                        .param("password", "wrong"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }
}