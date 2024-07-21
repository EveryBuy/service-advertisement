package ua.everybuy.routing.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WakeUpController {
    @GetMapping("/ad/keep-alive")
    public String keepAlive() {
        return "Service is active";
    }
}
