package com.example.serverstatus.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String home() {
        return "<h1>Welcome to Server Status Checker!</h1>"
            + "<p>The application is running.</p>"
            + "<ul>"
            + "<li><a href=\"/checks/status/google\">Check Google status (JSON)</a></li>"
            + "<li><a href=\"/checks/status/wikipedia\">Check Wikipedia status (JSON)</a></li>"
            + "<li><a href=\"/checks/status/microsoft\">Check Microsoft status (JSON)</a></li>"
            + "<li><a href=\"/checks/status/spotify\">Check Spotify status (JSON)</a></li>"
            + "</ul>";
    }

    @GetMapping(value = "/error", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String error() {
        return "<h1>404 - Page Not Found</h1>"
            + "<p>The page you are looking for does not exist.</p>"
            + "<p>Return to the <a href=\"/\">home page</a>.</p>";
    }
}
