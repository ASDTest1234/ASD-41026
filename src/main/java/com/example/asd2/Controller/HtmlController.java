package com.example.asd2.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class HtmlController {

    // Redirect to the static HTML page when accessing /minh/tickets
    @GetMapping("/tickets")
    public RedirectView redirectToTicketsPage() {
        return new RedirectView("src/main/resources/templates/all_tickets_Staff.html"); // This should match the path to your HTML file
    }
}

