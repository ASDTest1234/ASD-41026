package com.example.asd2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;


@Component
public class CustomerAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Retrieve the roles from the authentication object
        Collection<?> authorities = authentication.getAuthorities();
        String redirectURL = "";

        // Redirect based on user role
        if (authorities.stream().anyMatch(role -> role.toString().equals("ROLE_ADMIN"))) {
            redirectURL = "/admin/home_admin";
        } else if (authorities.stream().anyMatch(role -> role.toString().equals("ROLE_STAFF"))) {
            redirectURL = "/staff/home_staff";
        } else {
            redirectURL = "/user/home_user";
        }

        // Redirect the user to the appropriate URL
        response.sendRedirect(redirectURL);
    }
}
