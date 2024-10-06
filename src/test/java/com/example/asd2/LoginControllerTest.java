package com.example.asd2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testValidLogin() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "test@gmail.com")
                        .param("password", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/staff/home_staff"));
    }

    @Test
    public void testInvalidPerms() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "test@gmail.com")
                        .param("password", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/staff/home_staff"));

        mockMvc.perform(get("/staff/home_staff"))
                .andExpect(status().isOk())
                .andExpect(redirectedUrl("/staff/home_staff"));

//        mockMvc.perform(get("/admin/home_admin"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/staff/home_staff"));
    }

    @Test
    public void InvalidLogin() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "wrong")
                        .param("password", "wrong"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }
//
    @Test
    public void BruteForceURL() throws Exception {
        mockMvc.perform(get("/staff/home_staff"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
        mockMvc.perform(get("/user/home_user"))

                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
        mockMvc.perform(get("/admin/home_admin"))

                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void testLoginLogout() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "test@gmail.com")
                        .param("password", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/staff/home_staff"));

        mockMvc.perform(post("/logout"))

                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout"));
    }

    @Test
    public void testLoginLogoutInvalidPerms() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "test@gmail.com")
                        .param("password", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/staff/home_staff"));

        mockMvc.perform(post("/logout"))

                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout"));

        mockMvc.perform(get("/staff/home_staff"))

                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void testPerms() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "test@gmail.com")
                        .param("password", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/staff/home_staff"));


        mockMvc.perform(get("/admin/home_admin"))

                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }


}