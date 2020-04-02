package com.changgou.filter;

import com.changgou.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/29
 */
@Component
public class AuthirizeDilter implements GlobalFilter,Ordered{
    //定义常量
    private static final String AUTHORIZE_TOKEN = "Authorization";

    //登陆界面
    private static final String LOGIN_URL = "http://localhost:9001/oauth/login";
    /**
     * @Description 业务处理
     * @Author xingge
     * @Param      * @param exchange
     * @param chain
     * @Date 16:44 2020/2/29
     * @Version 2.1
     **/
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求和相应
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //判断用户是否是登录操作,如果是直接放行
        String path = request.getURI().getPath();
        if (path.startsWith("/api/user/login")) {
            return chain.filter(exchange);
        }
        //其他操作需要判断是否有token    请求头 请求参数 cookie
        // 2.1 从请求参数获取token
        String token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
        // 2.2 从请求头获取token
        if (StringUtils.isEmpty(token)){
            token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);
        }
        // 2.3 从cookie中获取token
        if (StringUtils.isEmpty(token)){
            HttpCookie cookie = request.getCookies().getFirst(AUTHORIZE_TOKEN);
            if (cookie != null){
                token = cookie.getValue();
            }
        }
        // 3、如果没有token，不放行
        if (StringUtils.isEmpty(token)){
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);    // 设置响应状态码
//            return response.setComplete();

            //将用户踢回到登陆界面
            String url = LOGIN_URL + "?ReturnUrl=" + request.getURI().toString();
            response.getHeaders().add("Location", url);
            response.setStatusCode(HttpStatus.SEE_OTHER);   // 303  跳转到url
            return  response.setComplete();

        }
        // 4、token存在，需要解析token
        try {
            // 解析成功
//            Claims claims = JwtUtil.parseJWT(token);

            //手动添加头信息
            request.mutate().header("Authorization", "bearer " + token);
        } catch (Exception e) {
            e.printStackTrace();
            // 解析失败
            response.setStatusCode(HttpStatus.UNAUTHORIZED);    // 设置响应状态码
            return response.setComplete();
        }
        return chain.filter(exchange);
    }

    /**
     * @Description 执行顺序
     * @Author xingge
     * @Param      * @param
     * @Date 16:44 2020/2/29
     * @Version 2.1
     **/
    @Override
    public int getOrder() {
        return 0;
    }
}
