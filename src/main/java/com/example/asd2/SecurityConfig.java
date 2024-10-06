package com.example.asd2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/register", "/product/**").permitAll()
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                        .requestMatchers("/staff/**").hasAnyRole("STAFF" ,"ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN", "STAFF")
                        .requestMatchers("/minh/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(customerSuccessHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true) // Clear session upon logout
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable()) // Disabling CSRF for simplicity; be cautious with this in production
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/Error")
                )
                .build();
    }



//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.builder()
//                .username("user@gmail.com")
//                .password("$2a$12$FgB4juuyZvKwapByhkBLS.tABGOtjmdy/8kQ.NWKCdhPwkHVxBIxu")
//                .roles("USER")
//                .build();
//        UserDetails staff = User.builder()
//                .username("staff@gmail.com")
//                .password("$2a$12$FgB4juuyZvKwapByhkBLS.tABGOtjmdy/8kQ.NWKCdhPwkHVxBIxu")
//                .roles("STAFF", "USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin@gmail.com")
//                .password("$2a$12$FgB4juuyZvKwapByhkBLS.tABGOtjmdy/8kQ.NWKCdhPwkHVxBIxu")
//                .roles("ADMIN", "USER", "STAFF")
//                .build();
//        return new InMemoryUserDetailsManager(user, staff, admin);
//
//    }


    @Bean
    public AuthenticationSuccessHandler customerSuccessHandler() {
        return new CustomerAuthenticationSuccessHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}