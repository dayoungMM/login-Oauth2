package com.moondy.loginoauth2.controller;

import com.moondy.loginoauth2.auth.config.SocialLoginType;
import com.moondy.loginoauth2.auth.domain.UserInfo;
import com.moondy.loginoauth2.auth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
@RequestMapping("/app/accounts")
@Slf4j
public class AuthController {

    private final OAuthService oAuthService;

    @GetMapping("/auth/{socialLoginType}") //GOOGLE이 들어올 것이다.
    public void socialLoginRedirect(@PathVariable(name = "socialLoginType") String socialLoginPath) throws IOException {
        SocialLoginType socialLoginType = SocialLoginType.valueOf(socialLoginPath.toUpperCase());
        oAuthService.request(socialLoginType);
    }

    @GetMapping(value = "/auth/{socialLoginType}/callback")
    public String callback(
            @PathVariable(name = "socialLoginType") String socialLoginPath,
            @RequestParam(name = "code") String code,
            RedirectAttributes re) throws IOException {
        log.info(">> 소셜 로그인 API 서버로부터 받은 code :" + code);
        SocialLoginType socialLoginType = SocialLoginType.valueOf(socialLoginPath.toUpperCase());
        UserInfo userInfo = oAuthService.oAuthLogin(socialLoginType, code);
        re.addAttribute("email", userInfo.getEmail());
        re.addAttribute("username", userInfo.getUserName());
        re.addAttribute("picture", userInfo.getPictureUrl());
        return "redirect:/home";
    }
}