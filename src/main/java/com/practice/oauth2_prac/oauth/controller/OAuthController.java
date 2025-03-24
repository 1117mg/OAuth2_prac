package com.practice.oauth2_prac.oauth.controller;

import com.practice.oauth2_prac.global.jwt.JwtTokenProvider;
import com.practice.oauth2_prac.global.jwt.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/token/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("Authorization") String refreshHeader) {
        if (refreshHeader == null || !refreshHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Refresh token 누락");
        }

        String refreshToken = refreshHeader.substring(7);

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(401).body("유효하지 않은 refresh token");
        }

        String userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        Long userIdLong = Long.valueOf(userId);

        if (!refreshTokenService.isValid(userIdLong, refreshToken)) {
            return ResponseEntity.status(401).body("저장된 토큰과 불일치");
        }

        // 기존 refresh token 삭제
        refreshTokenService.delete(userIdLong);

        // 새로운 토큰 발급
        String newAccessToken = jwtTokenProvider.generateToken(userId);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(userId);

        long refreshExp = jwtTokenProvider.getExpiration(newRefreshToken).getTime() - System.currentTimeMillis();
        refreshTokenService.save(userIdLong, newRefreshToken, refreshExp);

        return ResponseEntity.ok(Map.of(
                "accessToken", newAccessToken,
                "refreshToken", newRefreshToken
        ));
    }
}
