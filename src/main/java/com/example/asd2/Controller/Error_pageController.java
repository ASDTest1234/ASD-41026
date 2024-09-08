package com.example.asd2.Controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Error_pageController implements ErrorController {// tells us that this class is used to handle Errors

    @GetMapping("/Error")// URL Path
    public String ErrorHandle(){
        return "Error";}// return HTML

}
