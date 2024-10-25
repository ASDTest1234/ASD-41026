

import com.example.asd2.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc

public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private UserService userservice;



// Unit Test




    // Integration test
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
        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(post("/login")
                        .param("username", "test@gmail.com")
                        .param("password", "123")
                        .session(session))
                .andExpect(status().is3xxRedirection());
        //.andExpect(redirectedUrl("/staff/home_staff"));

        mockMvc.perform(get("/staff/home_staff").session(session))
                .andExpect(status().isOk());
        //.andExpect(redirectedUrl("/staff/home_staff"));

        mockMvc.perform(get("/admin/home_admin").session(session))
                .andExpect(status().isForbidden());
        //.andExpect(redirectedUrl("/staff/home_staff"));
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
        MockHttpSession session = new MockHttpSession();
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