package com.example.asd2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomerAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirectURL;

        if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
            redirectURL = "/admin/home_admin";
        }
        else if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_STAFF"))){
            redirectURL = "/staff/home_staff";
        }
        else{
            redirectURL = "/user/home_user";
        }


        response.sendRedirect(redirectURL);
    }
}
