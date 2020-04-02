package com.changgou.oauth.config;

import com.changgou.oauth.util.JwtToken;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.concurrent.locks.ReadWriteLock;

@Component
public class FeignOauth2RequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 如果其微服务工程之间相互调用，那么也需要将令牌信息放入头中
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null){
            HttpServletRequest request = attributes.getRequest();
            // 获取所有的头信息
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null){
                while (headerNames.hasMoreElements()){
                    // 头信息的：名称（key）
                    String name = headerNames.nextElement();
                    // 获取头对应的value
                    String value = request.getHeader(name);
                    // 将信息放入RestTemplate中
                    requestTemplate.header(name, value);
                }
            }
        }
        // 将令牌放入请求头中(changgou-service-user工程需要)
        String token = JwtToken.adminJwt();// 创建令牌
        requestTemplate.header("Authorization", "bearer " + token);
    }


}