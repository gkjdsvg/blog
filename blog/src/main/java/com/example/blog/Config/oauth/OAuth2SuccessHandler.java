package com.example.blog.Config.oauth;

import com.example.blog.domain.RefreshToken;
import com.example.blog.domain.User;
import com.example.blog.jwt.TokenProvider;
import com.example.blog.repository.RefreshTokenRepository;
import com.example.blog.service.UserService;
import com.example.blog.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token"; //리프레시 쿠키 저장
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14); //14일까지 유효
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1); //하루까지 유효
    public static final String REDIRECT_PATH = "/articles"; //URI 경로

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal(); //oauth2 인증을 한 유저 정보 가져오기
        User user = userService.findByEmail((String) oAuth2User.getAttributes().get("email"));  //이메일을 사용해 유저 조회

        // 액세스 토큰 생성
        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);

        // 리프레시 토큰 생성 -> DB & 쿠키 저장
        String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION); //리프레시 토큰 생성
        saveRefreshToken(user.getId(), refreshToken); // 유저 아이디랑 리프레시 토큰을 DB에 저장
        addRefreshTokenTOCookie(request, response, refreshToken); //리프레시 토큰을 쿠키에 저장

        // 액세스 토큰을 헤더에 추가하여 응답
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setStatus(HttpServletResponse.SC_OK); // 성공 응답 코드
    }

    //생성된 리프레시 토큰을 전달 받아 데이터베이스 저장
    private void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserid(userId) //유저 아이디로 리프레시 토큰 찾기
                .map(entity -> entity.update(newRefreshToken)) //기존 리프레시 토큰을 새로운 리프레시 토큰으로 업데이트
                .orElse(new RefreshToken(userId, newRefreshToken)); //없으면 새로 생성
        refreshTokenRepository.save(refreshToken); //리프레시 토큰을 DB에 저장
    }

    //생성된 리프레시 토큰 쿠키 저장
    private void addRefreshTokenTOCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds(); //14일을 초 단위로 바꾸기
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME); //기존 리프레시 토큰이 저장된 쿠키 삭제
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge); //새로 생성된 리프레시 토큰을 쿠키에 저장
    }

    //인증 관련 설정값, 쿠기 제거
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request); //리퀘스트를 지워,,
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response); //리퀘스트 쿠키에서 리퀘스트와 리스폰스를 지워?
    }

    //액세스 토큰을 패스에 추가
    private String getTargetUrl(String token) {
        return UriComponentsBuilder.fromUriString(REDIRECT_PATH) //
                .queryParam("token", token)
                .build().toUriString();
    }
}
