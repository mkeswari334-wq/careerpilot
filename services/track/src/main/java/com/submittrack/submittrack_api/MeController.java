package com.submittrack.submittrack_api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeController {
    @GetMapping("/api/me")
    public String me() {
        return "its me!";
    }
}
