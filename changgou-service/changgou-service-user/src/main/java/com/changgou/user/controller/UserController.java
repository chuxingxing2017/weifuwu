package com.changgou.user.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.user.pojo.User;
import com.changgou.user.service.UserService;
import entity.BCrypt;
import entity.JwtUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/29
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired(required = false)
    UserService userService;

    /**
     * @Description 增加用户积分
     * @Author xingge
     * @Param      * @param username
     * @param point
     * @Date 18:02 2020/3/4
     * @Version 2.1
     **/
    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/incrUserPoints/{username}/{point}")
    public Result incrUserPoints(@PathVariable(value = "username") String username,
                                 @PathVariable(value = "point") Integer point){
        userService.incrUserPonits(username, point);
        return new Result(true, StatusCode.OK, "增加用户积分成功");
    }

    /**
     * @Description 用户登录
     * @Author xingge
     * @Param      * @param username
 * @param password
 * @param response
     * @Date 9:53 2020/3/14
     * @Version 2.1
     **/

    @RequestMapping("/login")
    public Result login(String username, String password, HttpServletResponse response) {
        User user = userService.findById(username);
        if (user != null && BCrypt.checkpw(password,user.getPassword())) {
            //生成token
            HashMap<String, Object> map = new HashMap<>();
            map.put("role", "USER");
            map.put("status", "SUCCESS");
            map.put("userinfo", user);
            String info = JSON.toJSONString(map);
            String token = JwtUtil.createJWT(UUID.randomUUID().toString(), info, null);

            //用户登陆成功后,生成token并且保存到cookie中
            Cookie cookie = new Cookie("Authorization",token);
            cookie.setDomain("localhost");
            cookie.setPath("/");
            response.addCookie(cookie);

            return new Result(true, StatusCode.OK, "登陆成功");
        }
        return new Result(false, StatusCode.LOGINERROR, "登录失败");
    }
//    @PreAuthorize("hasAnyAuthority('admin','user')")
    @GetMapping("/findAll")
    public Result findAll() {
        try {
            List<User> list = userService.findAll();
            return new Result(true, StatusCode.OK, "查询成功",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "查询失败");
        }
    }

    /**
     * @Description 获取单个用户的所有信息
     * @Author xingge
     * @Param      * @param id
     * @Date 9:54 2020/3/14
     * @Version 2.1
     **/
    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/{id}")
    public Result<User> findById(@PathVariable(name = "id") String id) {
        User user = userService.findById(id);
        return new Result<>(true, StatusCode.OK, "查询成功", user);
    }
}
