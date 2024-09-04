package com.example.asd2.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {

    @GetMapping("/home")
    public String handleWelcome(){
        return "home";
    }

    @GetMapping("/home_admin")
    public String handleAdmin(){
        return "home_admin";
    }
    @GetMapping("/home_user")
    public String handleUser(){
        return "home_user";
    }

    @GetMapping("/login")
    public String handleLogin(){
        return "login";
    }



}
