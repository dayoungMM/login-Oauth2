package com.moondy.loginoauth2.controller;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @GetMapping("/home")
    public String index(@RequestParam @Nullable String username, @RequestParam @Nullable String email, @RequestParam @Nullable String picture, Model model) {
        if (username != null) {
            model.addAttribute("username", username);
        }
        if (email != null) {
            model.addAttribute("email", email);
        }
        if (picture != null) {
            model.addAttribute("picture", picture);
        }
        return "index";
    }

}
