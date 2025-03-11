package com.example.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuth2Controller {
    @GetMapping("/login")
    public String login() {
        String googleAuthUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=1090860626822-3elf6jm8vtbivfap7778jbqf0lmv97a9.apps.googleusercontent.com&redirect_uri=http://localhost:8084/login/oauth2/code/google&response_type=code&scope=profile email";
        return "redirect:" + googleAuthUrl;
    }
}
