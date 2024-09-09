package com.example.asd2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomerAuthenticationSuccessHandler implements AuthenticationSuccessHandler { // implements the AuthenticationSuccessHandler from Spring Security
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirectURL; // creating a variable to hold where to redirect the users.

        //getAuthorities() == gets the collection of authorities/ roles and permissions of users
        //.stream() ==
        //anyMatch() == used to check the output from the stream
        //a -> a.getAuthority.equals("ROLE_ADMIN") == a lambda expression that gets the authority and returns a bool answer, ROLE_ will always be appended to roles given from the SecurityConfig UserDetailService.

        if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){// basically
            redirectURL = "/admin/home_admin";
        }
        else if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_STAFF"))){
            redirectURL = "/staff/home_staff";
        }
        else{
            redirectURL = "/user/home_user";
        }


        response.sendRedirect(redirectURL); // just redirects the User based on URL.
    }
}
