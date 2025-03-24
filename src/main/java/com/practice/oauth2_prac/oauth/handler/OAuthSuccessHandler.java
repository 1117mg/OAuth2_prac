package com.practice.oauth2_prac.oauth.handler;

import com.practice.oauth2_prac.global.jwt.JwtTokenProvider;
import com.practice.oauth2_prac.global.jwt.RefreshTokenService;
import com.practice.oauth2_prac.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        var oauthUser = (org.springframework.security.oauth2.core.user.DefaultOAuth2User) authentication.getPrincipal();

        Object idAttr = oauthUser.getAttribute("id");

        if (idAttr == null) {
            throw new IllegalArgumentException("소셜 ID가 null입니다.");
        }

        String socialId = String.valueOf(idAttr);
        var user = userRepository.findBySocialId(socialId).orElseThrow();

        String accessToken = jwtTokenProvider.generateToken(user.getId().toString());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId().toString());

        long refreshExp = jwtTokenProvider.getExpiration(refreshToken).getTime() - System.currentTimeMillis();
        refreshTokenService.save(user.getId(), refreshToken, refreshExp);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write("{\"accessToken\": \"" + accessToken + "\", \"refreshToken\": \"" + refreshToken + "\"}");
        response.getWriter().flush();
    }
}