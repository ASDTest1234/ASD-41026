//package com.example.asd2;
//
//
////import com.example.asd2.Implementation.CustomUserDetailsService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityfilterChain(HttpSecurity http) throws Exception { // is the security configuration and tells the system what to do.
////        return http// intercepts all URLS and filters them based on the configuration.
////                .authorizeHttpRequests(authorise ->{
////                    authorise.requestMatchers("/login").permitAll();// allows anyone to access this page
////                    authorise.requestMatchers("/users/**").permitAll();
////                    authorise.requestMatchers("/admin/**").hasRole("ADMIN");// allows Admin to enter into any URl with the /admin and has the role admin
////                    authorise.requestMatchers("/staff/**").hasRole("STAFF");// allows staff to enter into any URl with the /staff and has the role staff
////                    authorise.requestMatchers("/user/**").hasRole("USER");// allows user to enter into any URl with the /user and has the role user
////                    authorise.anyRequest().authenticated(); // makes sure that the users have to be authenticated before they can access other page.
////
////                })
////                .formLogin(form -> form
////                        .loginPage("/login")// tells springboot where the login.html is
////                        .successHandler(customerSuccessHandler())// tells springboot what to do when login is successful
////                        .permitAll()
////                )
////
////                .logout((logout) -> logout
////                        .logoutUrl("/logout")// Log out URL
////                        .permitAll()
////
////                )
////                .csrf(csrf -> csrf.disable())
////                .build();
//
//        return http
//                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
//                .csrf(csrf -> csrf.disable()) // Disable CSRF for debugging
//                .logout(logout -> logout.permitAll())
//                .build();
//    }
//
////    @Bean
////    public UserDetailsService userDetailsService() {
////        return new CustomUserDetailsService();  // Use your custom UserDetailsService
////    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.builder()
//                .username("user")
//                .password("$2a$12$QhW1s7GaY7sOSkSPoav4NO5YRwFVad.ae5u918MBm78mnOfw2KOia")
//                .roles("USER")
//                .build();
//        UserDetails staff = User.builder()
//                .username("staff")
//                .password("$2a$12$9JO7Og/uxLKiK0KAdQmgzOKLN.JNNHOQmhRvZP.Kwwxqp/dGMfRGy")
//                .roles("STAFF", "USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("$2a$12$Zkl61eMGwmcILuvZbgvi5ODZLX.ClxOow0lqaPHlFYQFSS1/GLeDm")
//                .roles("ADMIN", "USER", "STAFF")
//                .build();
//        return new InMemoryUserDetailsManager(user, staff, admin);
//
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() { // password Enconder using BCRypt hashing so passwords aren't stored in plaintext
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationSuccessHandler customerSuccessHandler() { // Tells the where to redirect the users when they are logged in.
//        return new CustomerAuthenticationSuccessHandler();
//    }
//
//}

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
                        .requestMatchers("/login", "/register", "/product/**", "/Search").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/staff/**").hasRole("STAFF")
                        .requestMatchers("/user/**").hasRole("USER")
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