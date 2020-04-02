package com.changgou.oauth.service.impl;

import com.changgou.oauth.service.UserLoginService;
import com.changgou.oauth.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/3/2
 */
@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired   //
    LoadBalancerClient loadBalancerClient;

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
    @Override
    public AuthToken login(String username, String password, String grant_type, String clientId, String clientSecret) {

        try {
            // 调用oauth提供的url接口 http://localhost:9001/oauth/token
            // /oauth/token是oauth提供的地址
            //String url = "http://localhost:9001/oauth/token";
            ServiceInstance serviceInstance = loadBalancerClient.choose("user-auth");
            String uri = serviceInstance.getUri().toString();
            String url = uri + "/oauth/token";

            //通过resttmpalate进行调用
            LinkedMultiValueMap headers = new LinkedMultiValueMap<>();  //封装头信息
            byte[] encode = Base64.getEncoder().encode((clientId + ":" + clientSecret).getBytes());
            String encodeBase64 = new String(encode, "UTF-8");
            headers.add("Authorization","Basic "+encodeBase64);

            //封装body信息
            LinkedMultiValueMap body = new LinkedMultiValueMap<>();
            body.add("grant_type", grant_type);
            body.add("username", username);
            body.add("password", password);

            HttpEntity requestEntity = new HttpEntity(body, headers);
            ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);
            //结果集处理
            Map<String,Object> map = responseEntity.getBody();
            AuthToken authToken = new AuthToken();
            authToken.setAccessToken(map.get("access_token").toString());
            authToken.setRefreshToken(map.get("refresh_token").toString());
            authToken.setJti(map.get("jti").toString());
            return authToken;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("用户校验异常");
        }

    }
}
