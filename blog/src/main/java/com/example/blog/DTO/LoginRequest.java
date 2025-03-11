package com.example.blog.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    // Getter, Setter 추가
    private String email;
    private String password;

    public Object getUsername() {
        return email;
    }
}
