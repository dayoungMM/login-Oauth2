package com.moondy.loginoauth2.auth.service;


import com.moondy.loginoauth2.auth.config.SocialLoginType;
import com.moondy.loginoauth2.auth.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService {
    private final GoogleOauth googleOauth;
    private final HttpServletResponse response;

    public void request(SocialLoginType socialLoginType) throws IOException {
        String redirectURL;
        switch (socialLoginType) {
            case GOOGLE: {
                //각 소셜 로그인을 요청하면 소셜로그인 페이지로 리다이렉트 해주는 프로세스이다.
                redirectURL = googleOauth.getOauthRedirectURL();
            }
            break;
            default: {
                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
            }

        }

        response.sendRedirect(redirectURL);
    }

    public UserInfo oAuthLogin(SocialLoginType socialLoginType, String code) throws IOException {
        UserInfo result;
        switch (socialLoginType) {
            case GOOGLE: {
                //구글로 일회성 코드를 보내 액세스 토큰이 담긴 응답객체를 받아옴
                ResponseEntity<String> accessTokenResponse = googleOauth.requestAccessToken(code);

                //BE 서버로 보내 기존에 존재하는 사용자인지 확인
                //액세스 토큰을 다시 구글로 보내 구글에 저장된 사용자 정보가 담긴 응답 객체를 받아온다.
                String response= googleOauth.requestUserInfoToBe(socialLoginType, accessTokenResponse.getBody());
                result = googleOauth.parseCommonResponse(response);
                break;
            }
            default: {
                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
            }
        }
        return result;
    }
}