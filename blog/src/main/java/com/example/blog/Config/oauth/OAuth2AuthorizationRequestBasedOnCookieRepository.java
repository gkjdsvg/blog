package com.example.blog.Config.oauth;

import com.example.blog.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.util.WebUtils;

public class OAuth2AuthorizationRequestBasedOnCookieRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    public final static String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";// oauth2의 인증 요청을 저장하는 거.
    public final static int COOKIE_EXPIRE_SECONDS = 18000; //쿠키의 유효 시간?

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {//
        Cookie cookie = WebUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);//쿠키를,,, 가져와 그러기 위한 리퀘스트와 저기다 저장을 하는 거야.
        return CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class);//쿠키와 클래스를 역직렬화를 해 왜 해?
    }

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return null;
    }

    //인증 요청 저장하는 거네
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) { //리퀘스트가 null 이라면
            removeAuthorizationRequestCookies(request, response); //리퀘스트 쿠키를 지워
            return;
        }
        System.out.println("🔹 OAuth2 Authorization Request 저장됨: " + authorizationRequest);
        CookieUtil.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtil.serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS); //쿠키를 추가하는데 답장과 인증 요청 저장한 것과 리퀘스트를 직렬화 해
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME); //쿠키를 지워
    }
}
