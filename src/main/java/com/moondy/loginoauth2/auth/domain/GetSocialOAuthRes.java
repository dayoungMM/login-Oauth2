package com.moondy.loginoauth2.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

//클라이언트로 보낼 jwtToken, accessToken등이 담긴 객체
@Getter
@Setter
@AllArgsConstructor
@Builder
public class GetSocialOAuthRes {
    String email;
    String userName;
}