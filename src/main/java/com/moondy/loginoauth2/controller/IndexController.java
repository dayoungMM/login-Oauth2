package com.moondy.loginoauth2.controller;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @GetMapping("/home")
    public String index(@RequestParam @Nullable String username, @RequestParam @Nullable String email, Model model) {
        if (username != null) {
            model.addAttribute("username", "Moon");
        }
        if (email != null) {
            model.addAttribute("email", "moon@gmail.com");
        }
        return "index";
    }

}
