package com.moondy.loginoauth2.auth.domain;

import lombok.Data;
import org.apache.catalina.User;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class CommonResponse {
    private int code;
    private String message;
    private HashMap<String, UserInfo> attribute;
}
