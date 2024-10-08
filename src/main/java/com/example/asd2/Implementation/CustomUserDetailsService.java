package com.example.asd2.Implementation;

import com.example.asd2.Model.Users;
import com.example.asd2.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Optional;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    //constructor to take the UserRepository so it can allow for queries
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // overriding a method from the UserDetails Service to take the custom requirements from the Database
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Users> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        //Create instance of User with the email,password and role
        return new User(
                user.get().getEmail(),
                user.get().getPassword(),
                Collections.singletonList(
                        new SimpleGrantedAuthority(user.get().getRole())
                )
        );
    }
}
