package com.example.blog.service;

import com.example.blog.domain.RefreshToken;
import com.example.blog.domain.User;
import com.example.blog.jwt.TokenProvider;
import com.example.blog.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;

    public String createNewAccessToken(String refreshToken) {
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }
        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserid();
        User user = userService.findById(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }

    public String createRefreshToken(String email) { // ✅ String email을 받음
        String token = generateRefreshTokenUsingSecureRandom();

        // ✅ 이메일을 이용해 User 객체 가져오기
        User user = userService.findByEmail(email);

        // ✅ user.getId() 사용 가능
        RefreshToken refreshToken = new RefreshToken(user.getId(), token);
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    public String generateRefreshTokenUsingSecureRandom() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32]; // 32바이트 길이, 필요에 따라 조정 가능
        secureRandom.nextBytes(randomBytes);
        // URL-safe Base64 인코딩 (패딩 없이)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
