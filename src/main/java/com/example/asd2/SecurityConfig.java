package com.example.asd2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/css/**", "/js/**").permitAll()  // Allow access to login and static resources
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // Admin URLs restricted to ROLE_ADMIN
                        .requestMatchers("/user/**").hasRole("USER")    // User URLs restricted to ROLE_USER
                        .anyRequest().authenticated()  // All other URLs require authentication
                )
                .formLogin(form -> form
                        .loginPage("/login")  // Custom login page
                        .defaultSuccessUrl("/home", true)  // Redirect after login success
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll  // Allow logout for everyone
                )
                .httpBasic(Customizer.withDefaults());  // Replaces the deprecated httpBasic()

        return http.build();
    }
}
