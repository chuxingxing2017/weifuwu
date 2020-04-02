package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/28
 */
@SpringBootApplication
@EnableEurekaClient
public class GatewayWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayWebApplication.class, args);
    }

    /**
     * @Description IP限流
     * @Author xingge
     * @Param      * @param exchange
     * @Date 21:04 2020/2/28
     * @Version 2.1
     **/
    @Bean(name = "ipKeyResolver")
    public KeyResolver userKeyResolver() {
        return new KeyResolver() {
            @Override
            public Mono<String> resolve(ServerWebExchange exchange) {
                //获取远程客户端IP
                String hostName = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
                System.out.println(hostName);
                return Mono.just(hostName);
            }
        };
    }
}
