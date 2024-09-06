package com.example.asd2.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {



    @GetMapping("/admin/home_admin")
    public String handleAdmin(){
        return "home_admin";
    }

    @GetMapping("/user/home_user")
    public String handleUser(){
        return "home_user";
    }

    @GetMapping("/staff/home_staff")
    public String handleStaff(){
        return "home_staff";
    }

    @GetMapping("logout")
    public String handleLogout(){return "logout";}
    @GetMapping("/login")
    public String handleLogin(){
        return "login";
    }



}
