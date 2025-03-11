package com.example.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserViewController {

    @PostMapping("/signup")
    public String signup() {
        return "signup";
    }
}
