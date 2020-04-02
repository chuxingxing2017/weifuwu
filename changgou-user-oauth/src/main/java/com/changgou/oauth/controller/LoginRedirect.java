package com.changgou.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/oauth")
public class LoginRedirect {

    /**
     * @author 栗子
     * @Description 跳转用户登录页面
     * @Date 15:22 2019/8/24
     * @param
     * @return java.lang.String
     **/
    @GetMapping("/login")
    public String login(@RequestParam(value = "ReturnUrl", required = false) String ReturnUrl, Model model){
        model.addAttribute("ReturnUrl", ReturnUrl);
        return "login";
    }
}