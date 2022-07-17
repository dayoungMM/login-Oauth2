package com.moondy.loginoauth2.controller;

import com.moondy.loginoauth2.auth.config.SocialLoginType;
import javax.servlet.http.HttpSession;
import com.moondy.loginoauth2.auth.domain.GetSocialOAuthRes;
import com.moondy.loginoauth2.auth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RequiredArgsConstructor
@Controller
@RequestMapping("/app/accounts")
public class IndexController {

//    private final HttpSession httpSession;
    private final OAuthService oAuthService;

//    @GetMapping("/")
//    public String index(Model model) {
//
//        SessionUser user = (SessionUser) httpSession.getAttribute("user");
//
//        if(user != null){
//            model.addAttribute("username", user.getName());
//        }
//
//        return "index";
//    }

    /**
     * 유저 소셜 로그인으로 리다이렉트 해주는 url
     * [GET] /accounts/auth
     *
     * @return void
     */
//    @NoAuth
    @GetMapping("/auth/{socialLoginType}") //GOOGLE이 들어올 것이다.
    public void socialLoginRedirect(@PathVariable(name = "socialLoginType") String SocialLoginPath) throws IOException {
        SocialLoginType socialLoginType = SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
        oAuthService.request(socialLoginType);
    }

    @ResponseBody
    @GetMapping(value = "/auth/{socialLoginType}/callback")
    public GetSocialOAuthRes callback(
            @PathVariable(name = "socialLoginType") String socialLoginPath,
            @RequestParam(name = "code") String code) throws IOException {
        System.out.println(">> 소셜 로그인 API 서버로부터 받은 code :" + code);
        SocialLoginType socialLoginType = SocialLoginType.valueOf(socialLoginPath.toUpperCase());
        GetSocialOAuthRes getSocialOAuthRes = oAuthService.oAuthLogin(socialLoginType, code);
        return getSocialOAuthRes;
    }

}