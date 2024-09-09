package com.example.asd2;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // is the security configuration and tells the system what to do.
        return http// intercepts all URLS and filters them based on the configuration.
                .authorizeHttpRequests(authorise ->{
                    authorise.requestMatchers("/login").permitAll();// allows anyone to access this page
                    authorise.requestMatchers("/admin/**").hasRole("ADMIN");// allows Admin to enter into any URl with the /admin and has the role admin
                    authorise.requestMatchers("/staff/**").hasRole("STAFF");// allows staff to enter into any URl with the /staff and has the role staff
                    authorise.requestMatchers("/user/**").hasRole("USER");// allows user to enter into any URl with the /user and has the role user
                    authorise.anyRequest().authenticated(); // makes sure that the users have to be authenticated before they can access other page.

                })
                .formLogin(form -> form
                        .loginPage("/login")// tells springboot where the login.html is
                        .successHandler(customerSuccessHandler())// tells springboot what to do when login is successful
                        .permitAll()
                )

                .logout((logout) -> logout
                        .logoutUrl("/logout")// Log out URL
                        .permitAll()

                )
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {// basically like a in memory database, since not connected to database yet but will be used to authenticate the Users from database.
        UserDetails userss = User.builder()
                .username("user@gmail.com")
                .password("$2a$12$QhW1s7GaY7sOSkSPoav4NO5YRwFVad.ae5u918MBm78mnOfw2KOia")
                .roles("USER")
                .build();
        UserDetails staff = User.builder()
                .username("staff@gmail.com")
                .password("$2a$12$9JO7Og/uxLKiK0KAdQmgzOKLN.JNNHOQmhRvZP.Kwwxqp/dGMfRGy")
                .roles("STAFF", "USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin@gmail.com")
                .password("$2a$12$Zkl61eMGwmcILuvZbgvi5ODZLX.ClxOow0lqaPHlFYQFSS1/GLeDm")
                .roles("ADMIN", "USER", "STAFF")
                .build();
        return new InMemoryUserDetailsManager(userss, staff, admin);

    }

    @Bean
    public PasswordEncoder passwordEncoder() { // password Enconder using BCRypt hashing so passwords aren't stored in plaintext
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler customerSuccessHandler() { // Tells the where to redirect the users when they are logged in.
        return new CustomerAuthenticationSuccessHandler();
    }

}
