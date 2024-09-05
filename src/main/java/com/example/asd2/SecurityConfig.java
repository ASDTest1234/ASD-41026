package com.example.asd2;

import com.example.asd2.Model.Product;
import com.example.asd2.Model.ProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorise ->{
                    authorise.requestMatchers("/home").permitAll();
                    authorise.requestMatchers("/login").permitAll();
                    authorise.requestMatchers("/admin/**").hasRole("ADMIN");
                    authorise.requestMatchers("/staff/**").hasRole("STAFF");
                    authorise.requestMatchers("/user/**").hasRole("USER");
                    authorise.anyRequest().authenticated();

                })
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(new AuthenticationSuccessfulHandler()))

                .logout(logout -> logout.permitAll())
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password("$2a$12$QhW1s7GaY7sOSkSPoav4NO5YRwFVad.ae5u918MBm78mnOfw2KOia")
                .roles("USER")
                .build();
        UserDetails staff = User.builder()
                .username("staff")
                .password("$2a$12$9JO7Og/uxLKiK0KAdQmgzOKLN.JNNHOQmhRvZP.Kwwxqp/dGMfRGy")
                .roles("STAFF", "USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password("$2a$12$Zkl61eMGwmcILuvZbgvi5ODZLX.ClxOow0lqaPHlFYQFSS1/GLeDm")
                .roles("ADMIN", "USER", "STAFF")
                .build();
        return new InMemoryUserDetailsManager(user, staff, admin);

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ProductService productService() {
        return List::of;
    }

}
