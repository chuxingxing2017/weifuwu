package com.changgou.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @ClassName FeignInterceptor
 * @Description 将请求头信息放入feign予以服务间调用
 * @Author 传智播客
 * @Date 16:04 2020/3/2
 * @Version 2.1
 **/
@Configuration
public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        // 服务间需要认证，获取请求头相关数据
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            // 获取这些头信息
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String headName = headerNames.nextElement();        // 请求头名称
                    String headerValue = request.getHeader(headName);   // 请求头名称对应的value
                    template.header(headName, headerValue);
                }
            }
        }
    }
}
