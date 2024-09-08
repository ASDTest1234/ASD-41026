package com.example.asd2.Controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Error_pageController implements ErrorController {

    @GetMapping("/Error")
    public String Error(){
        return "Error";}

}
