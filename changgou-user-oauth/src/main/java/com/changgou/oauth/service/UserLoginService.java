package com.changgou.oauth.service;

import com.changgou.oauth.util.AuthToken;

import java.util.Map;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/3/2
 */

public interface UserLoginService {
    /**
     * @Description 用户登录认证
     * @Author xingge
     * @Param      * @param username
     * @param password
     * @param grant_type
     * @param clientId
     * @param clientSecret
     * @Date 20:17 2020/3/2
     * @Version 2.1
     **/
    AuthToken login(String username, String password, String grant_type, String clientId, String clientSecret);
}
