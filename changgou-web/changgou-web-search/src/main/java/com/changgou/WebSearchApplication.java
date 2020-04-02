package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/28
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.changgou.search.feign"})
public class WebSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebSearchApplication.class, args);
    }
}
