package com.moondy.loginoauth2.auth.domain;

import java.io.Serializable;
import lombok.Getter;

//Session에 사용자 정보를 저장하기 위한 Dto (세션에 저장할 수 있게 직렬화 기능 포함)

@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}