package com.changgou.order;

import entity.FeignInterceptor;
import entity.IdWorker;
import entity.TokenDecode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ClassName OrderApplication
 * @Description 订单启动类
 * @Author 传智播客
 * @Date 14:24 2020/3/2
 * @Version 2.1
 **/
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.changgou.order.dao"})
@EnableFeignClients(basePackages = {"com.changgou.goods.feign","com.changgou.user.feign"})
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }


//    @Bean
//    public FeignInterceptor feignInterceptor(){
//        return new FeignInterceptor();
//    }

    @Bean//管理IDwork
    public IdWorker idWorker() {
        return new IdWorker(1, 1);
    }

    @Bean  //获取用户登陆的信息
    public TokenDecode tokenDecode() {
        return new TokenDecode();
    }
}
