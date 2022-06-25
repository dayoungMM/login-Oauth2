package com.moondy.loginoauth2.auth.domain;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
        String nameAttributeKey, String name,
        String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey= nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    // Map을 받아서
    public static OAuthAttributes of(String registrationId,
        String userNameAttributeName,
        Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    //OAuthAttributes로 변환
    private static OAuthAttributes ofGoogle(String userNameAttributeName,
        Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                   .name((String) attributes.get("name"))
                   .email((String) attributes.get("email"))
                   .picture((String) attributes.get("picture"))
                   .attributes(attributes)
                   .nameAttributeKey(userNameAttributeName)
                   .build();
    }
    //User Entity로 변환
    public User toEntity() {
        return User.builder()
                   .name(name)
                   .email(email)
                   .picture(picture)
                   .role(Role.GUEST) //가입할 때 기본 권한은 GUEST
                   .build();
    }
}

