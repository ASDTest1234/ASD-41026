package com.example.asd2;

import com.example.asd2.Model.Product;
import com.example.asd2.Model.ProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.List;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/css/**", "/js/**").permitAll()  // Allow access to login and static resources
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // Admin URLs restricted to ROLE_ADMIN
                        .requestMatchers("/user/**").hasRole("USER")    // User URLs restricted to ROLE_USER
                        .requestMatchers("/staff/**").hasRole("STAFF")  // Staff URLs restricted to ROLE_STAFF
                        .anyRequest().authenticated()  // All other URLs require authentication
                )
                .formLogin(form -> form
                        .loginPage("/login")  // Custom login page
                        .successHandler(customSuccessHandler())  // Use custom success handler for redirection
                        .permitAll()
                )
                .logout(logout -> logout.permitAll());  // Allow logout for everyone


        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return (request, response, authentication) -> {
            // Redirect based on the user's role
            String redirectUrl = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")) ? "/home_admin"
                    : authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_STAFF")) ? "/home"
                    : "/home_user";

            response.sendRedirect(redirectUrl);
        };
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("password")
                .roles("ADMIN")
                .build();

        UserDetails staff = User.withDefaultPasswordEncoder()
                .username("staff")
                .password("password")
                .roles("STAFF")
                .build();

        return new InMemoryUserDetailsManager(userDetails,admin,staff);
    }

    @Bean
    public ProductService productService() {
        return List::of;
    }
}
