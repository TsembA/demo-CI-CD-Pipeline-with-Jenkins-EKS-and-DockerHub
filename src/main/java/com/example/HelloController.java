package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "✅ Java Maven app is running on EKS!";
    }

    @GetMapping("/status")
    public String status() {
        return "App Status: OK";
    }
}
