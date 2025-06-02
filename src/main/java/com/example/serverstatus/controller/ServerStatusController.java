package com.example.serverstatus.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class ServerStatusController {

    @GetMapping
    public String checkStatus() {
        return "Server is running";
    }
}
