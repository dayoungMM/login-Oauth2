package com.moondy.loginoauth2.auth.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moondy.loginoauth2.auth.config.SocialLoginType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

//@AllArgsConstructor
@Component
@RequiredArgsConstructor
public class GoogleOauth implements SocialOauth {

    private final RestTemplate restTemplate;

    //applications.yml 에서 value annotation을 통해서 값을 받아온다.
    @Value("${spring.oauth2.google.url}")
    private String GOOGLE_SNS_LOGIN_URL;

    @Value("${spring.oauth2.google.client-id}")
    private String GOOGLE_SNS_CLIENT_ID;

    @Value("${spring.oauth2.google.callback-url}")
    private String GOOGLE_SNS_CALLBACK_URL;

    @Value("${spring.oauth2.google.client-secret}")
    private String GOOGLE_SNS_CLIENT_SECRET;

    @Value("${spring.oauth2.google.scope}")
    private String GOOGLE_DATA_ACCESS_SCOPE;

    private final ObjectMapper objectMapper;

    @Override
    public String getOauthRedirectURL() {

        Map<String, Object> params = new HashMap<>();
        params.put("scope", GOOGLE_DATA_ACCESS_SCOPE);
        params.put("response_type", "code");
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);

        //parameter를 형식에 맞춰 구성해주는 함수
        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));
        String redirectURL = GOOGLE_SNS_LOGIN_URL + "?" + parameterString;
        System.out.println("redirectURL = " + redirectURL);

        return redirectURL;
    }

    public ResponseEntity<String> requestAccessToken(String code) {
        String googleTokenRequestUrl = "https://oauth2.googleapis.com/token";
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(googleTokenRequestUrl,
                params, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity;
        }
        return null;
    }

    public GoogleOAuthToken getAccessToken(ResponseEntity<String> response) throws JsonProcessingException {
        System.out.println("response.getBody() = " + response.getBody());
        GoogleOAuthToken googleOAuthToken = objectMapper.readValue(response.getBody(), GoogleOAuthToken.class);
        return googleOAuthToken;
    }

    public ResponseEntity<String> requestUserInfo(GoogleOAuthToken oAuthToken) {
        String googleUserinfoRequestUrl="https://www.googleapis.com/oauth2/v1/userinfo";

        //header에 accessToken을 담는다.
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+oAuthToken.getAccess_token());
        System.out.println("Authorization: " + "Bearer "+oAuthToken.getAccess_token());

        //HttpEntity를 하나 생성해 헤더를 담아서 restTemplate으로 구글과 통신하게 된다.
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                googleUserinfoRequestUrl,
                HttpMethod.GET,
                request,
                String.class
        );

        System.out.println("response.getBody() = " + response.getBody());
        return response;
    }

    public GoogleUser getUserInfo(ResponseEntity<String> userInfoRes) throws JsonProcessingException {
        GoogleUser googleUser = objectMapper.readValue(userInfoRes.getBody(), GoogleUser.class);
        System.out.println(googleUser.toString());
        return googleUser;
    }

    public UserInfo parseCommonResponse (String respone) throws JsonProcessingException, RuntimeException {
        CommonResponse commonResponse = objectMapper.readValue(respone, CommonResponse.class);
        // 실제로는 유저가 이미 있는 경우에는 로그인에 성공한 것으로 판단하여 JWT 토큰을 발행할 예정
        // 하지만 지금은 임시 테스트니깐 예외처리
        if (commonResponse.getCode() == -1002) throw  new RuntimeException(commonResponse.getMessage());
        UserInfo oAuthRes = commonResponse.getAttribute().get("userInfo");
        return oAuthRes;
    }

    public String requestUserInfoToBe(SocialLoginType socialLoginType, String code) {
        String userServerUrl = String.format("http://localhost:9090/auth/oauth/%s?code=%s",socialLoginType.toString(), code);

        HttpEntity request = new HttpEntity(null);

        ResponseEntity<String> response = restTemplate.exchange(
                userServerUrl,
                HttpMethod.GET,
                request,
                String.class
        );
        return response.getBody();
    }
}