//package com.example.asd2.Implementation;
//
//import com.example.asd2.Model.Users;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.Collections;
//
//public class CustomUserDetails implements UserDetails {
//
//    private final Users user;
//
//    public CustomUserDetails(Users user) {
//        this.user = user;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));
//    }
//
//    @Override
//    public String getPassword() {
//        return user.getPassword(); // Return the password stored in MongoDB
//    }
//
//    @Override
//    public String getUsername() {
//        return user.getEmail(); // Use email instead of username for authentication
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
