


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.hamcrest.Matchers.*;

import com.example.asd2.Model.Products;
import com.example.asd2.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.mock.web.MockHttpSession;

import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
public class UserBrowsingTest {

    @Autowired
    private MockMvc mockMvc;

    private UserService userService;


    @Test
    public void testSearchProductsByKeywords() throws Exception {
        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(post("/login")
                        .param("username", "user@gmail.com")
                        .param("password", "123")
                        .session(session))

                .andExpect(status().is3xxRedirection());


        String valueAsString = "123.2";
        BigDecimal bigDecimalValue = new BigDecimal(valueAsString);


        mockMvc.perform(get("/user/home_user")
                        .session(session)
                        .param("filter", "Pasta"))

                .andExpect(status().isOk())  // Expect HTTP 200 OK
                .andExpect(view().name("home_user"))  // Expect the "home_user" view to be returned
                .andExpect(model().attribute("products", hasSize(1))) ; // Assert that one product is returned
//                .andExpect(model().attribute("products", containsInAnyOrder(
//                        new Products("66d8131405d3babc8ea47fb5", "Pasta", "Pasta produced from the finest pastafarians", 100, bigDecimalValue, "staple","admin123"))  // Assert product details
//                ));
    }

    @Test
    public void testSearchProductsByWrongName() throws Exception {
        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(post("/login")
                        .param("username", "user@gmail.com")
                        .param("password", "123")
                        .session(session))

                .andExpect(status().is3xxRedirection());


        String valueAsString = "123.2";
        BigDecimal bigDecimalValue = new BigDecimal(valueAsString);


        mockMvc.perform(get("/user/home_user")
                        .session(session)
                        .param("filter", "Wrong")) // putting in a result that returns nothing.

                .andExpect(status().isOk())  // Expect HTTP 200 OK
                .andExpect(view().name("home_user"))  // Expect the "home_user" view to be returned
                .andExpect(model().attribute("products", hasSize(0)));  // Assert that one product is returned

    }



    @Test
    public void testBrowsing() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/login")
                        .param("username", "user@gmail.com")
                        .param("password", "123")
                        .session(session)) // Using the mock session
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/user/home_user")
                        .param("keyword", "Pasta")
                        .session(session)) // Using the same session
                .andExpect(status().isOk())
                .andExpect(view().name("home_user")); // Expecting to see the home_user view

        mockMvc.perform(get("/staff/home_staff")
                        .param("keyword", "Pasta")
                        .session(session)) // Using the same session
                .andExpect(status().isForbidden());

    }

    @Test
    public void testDefaultProductsTable() throws Exception {
        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(post("/login")
                        .param("username", "user@gmail.com")
                        .param("password", "123")
                        .session(session))

                .andExpect(status().is3xxRedirection());


        //getting the same URL but posting a result of wrong
        mockMvc.perform(get("/user/home_user")
                        .session(session))

                .andExpect(status().isOk())  // Expect HTTP 200 OK
                .andExpect(view().name("home_user"));  // Expect the "home_user" view to be returned

    }

    @Test
    public void testForbiddenURLs() throws Exception {
        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(post("/login")
                        .param("username", "user@gmail.com")
                        .param("password", "123")
                        .session(session))

                .andExpect(status().is3xxRedirection());


        //getting the same URL but posting a result of wrong
        mockMvc.perform(get("/staff/home_staff")
                        .session(session))
                .andExpect(status().isForbidden());  // Expect HTTP 200 OK

        //getting the same URL but posting a result of wrong
        mockMvc.perform(get("/admin/home_admin")
                        .session(session))
                .andExpect(status().isForbidden());  // Expect HTTP 200 OK

    }

    @Test
    public void testAdminURLs() throws Exception {
        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(post("/login")
                        .param("username", "admin@gmail.com")
                        .param("password", "123")
                        .session(session))

                .andExpect(status().is3xxRedirection());


        //getting the same URL but posting a result of wrong
        mockMvc.perform(get("/staff/home_staff")
                        .session(session))
                .andExpect(status().isOk());  // Expect HTTP 200 OK

        //getting the same URL but posting a result of wrong
        mockMvc.perform(get("/user/home_user")
                        .session(session))
                .andExpect(status().isOk());  // Expect HTTP 200 OK

    }

    @Test
    public void testStaffURLs() throws Exception {
        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(post("/login")
                        .param("username", "test@gmail.com")
                        .param("password", "123")
                        .session(session))

                .andExpect(status().is3xxRedirection());


        //getting the same URL but posting a result of wrong
        mockMvc.perform(get("/admin/home_admin")
                        .session(session))
                .andExpect(status().isForbidden());  // Expect HTTP 200 OK

        //getting the same URL but posting a result of wrong
        mockMvc.perform(get("/user/home_user")
                        .session(session))
                .andExpect(status().isOk());  // Expect HTTP 200 OK

    }

    @Test
    public void testUserURLs() throws Exception {
        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(post("/login")
                        .param("username", "user@gmail.com")
                        .param("password", "123")
                        .session(session))

                .andExpect(status().is3xxRedirection());


        //getting the same URL but posting a result of wrong
        mockMvc.perform(get("/admin/home_admin")
                        .session(session))
                .andExpect(status().isForbidden());  // Expect HTTP 200 OK

        //getting the same URL but posting a result of wrong
        mockMvc.perform(get("/staff/home_staff")
                        .session(session))
                .andExpect(status().isForbidden());  // Expect HTTP 200 OK

    }




}