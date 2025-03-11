package com.example.blog.repository;

import com.example.blog.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserid(Long userId);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
