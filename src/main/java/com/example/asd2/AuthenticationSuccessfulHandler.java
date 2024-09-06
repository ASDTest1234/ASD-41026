//package com.example.asd2;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
//
//import java.io.IOException;
//
//public class AuthenticationSuccessfulHandler extends SavedRequestAwareAuthenticationSuccessHandler {
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
//
//        boolean isStaff = authentication.getAuthorities().stream()
//                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_STAFF"));
//
//        if(isStaff) {
//            setDefaultTargetUrl("/staff/home_staff");
//        }
//        else{
//            setDefaultTargetUrl("/user/home_user");
//        }
//
//        super.onAuthenticationSuccess(request, response, authentication);
//    }
//}
