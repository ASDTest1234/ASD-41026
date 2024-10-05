package com.example.asd2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component
public class CustomerAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomerAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        logger.info("Authentication successful for user: {}", username);


        request.getSession().setAttribute("username", username);


        if (!response.isCommitted()) {

            String redirectURL = request.getContextPath();
            if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
                redirectURL = "/admin/home_admin";
            } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_STAFF"))) {
                redirectURL = "/staff/home_staff";
            } else {
                redirectURL = "/user/home_user";
            }


            response.sendRedirect(redirectURL);
        } else {
            logger.warn("Response already committed; unable to perform redirect.");
        }
    }
}
