package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "<html><body style='text-align:center; font-family:sans-serif;'>"
             + "<h1>✅ Java Maven app is running on <span style='color:#4CAF50;'>EKS</span>!</h1>"
             + "<img src='https://cdn-icons-png.flaticon.com/512/919/919836.png' width='120' alt='Java Logo'/>"
             + "<p>Containerized with Docker & deployed to AWS EKS 🚀</p>"
             + "<h3>Uptime: <span id='uptime'>00:00:00</span></h3>"
             + "<script>\n"
             + "  let seconds = 0;\n"
             + "  function updateClock() {\n"
             + "    seconds++;\n"
             + "    const hrs = String(Math.floor(seconds / 3600)).padStart(2, '0');\n"
             + "    const mins = String(Math.floor((seconds % 3600) / 60)).padStart(2, '0');\n"
             + "    const secs = String(seconds % 60).padStart(2, '0');\n"
             + "    document.getElementById('uptime').textContent = `${hrs}:${mins}:${secs}`;\n"
             + "  }\n"
             + "  setInterval(updateClock, 1000);\n"
             + "</script>"
             + "</body></html>";
    }

    @GetMapping("/status")
    public String status() {
        return "<html><body style='text-align:center; font-family:sans-serif;'>"
             + "<h2>App Status: <span style='color:green;'>OK</span> ✅</h2>"
             + "<img src='https://cdn-icons-png.flaticon.com/512/5968/5968672.png' width='100' alt='Spring Boot'/>"
             + "</body></html>";
    }
}
