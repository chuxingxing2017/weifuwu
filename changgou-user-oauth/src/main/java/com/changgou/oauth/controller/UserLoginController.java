package com.changgou.oauth.controller;

import com.changgou.oauth.service.UserLoginService;
import com.changgou.oauth.service.impl.UserLoginServiceImpl;
import com.changgou.oauth.util.AuthToken;
import com.netflix.discovery.converters.Auto;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/3/2
 */
@RestController
@RequestMapping("/user")
public class UserLoginController {

    @Value("${auth.clientId}")
    private String clientId;

    @Value("${auth.clientSecret}")
    private String clientSecret;

    @Autowired(required = false)
    UserLoginService userLoginService;

    /**
     * @Description 用户认证
     * @Author xingge
     * @Param      * @param username
     * @param password
     * @Date 20:15 2020/3/2
     * @Version 2.1
     **/
    @RequestMapping("/login")
    public Result login(String username, String password, HttpServletResponse response) {
        AuthToken authToken = null;
        try {
            String grant_type = "password"; // 密码授权方式
            authToken = userLoginService.login(username, password, grant_type, clientId, clientSecret);
            // 将生成的token保存到cookie中
            String accessToken = authToken.getAccessToken();
            Cookie cookie = new Cookie("Authorization", accessToken);
            cookie.setDomain("localhost");
            cookie.setPath("/");
            response.addCookie(cookie);

            return new Result(true, StatusCode.OK, "登录成功", authToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, StatusCode.LOGINERROR, "登录失败");
    }
}
