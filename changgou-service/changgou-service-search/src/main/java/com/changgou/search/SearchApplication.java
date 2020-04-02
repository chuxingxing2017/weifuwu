package com.changgou.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/25
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients(basePackages = {"com.changgou.goods.feign"})
@EnableEurekaClient
@EnableElasticsearchRepositories(basePackages = {"com.changgou.search.dao"})
public class SearchApplication {
    public static void main(String[] args) {
        /**
         * Springboot整合Elasticsearch 在项目启动前设置一下的属性，防止报错
         * 解决netty冲突后初始化client时还会抛出异常
         * availableProcessors is already set to [12], rejecting [12]
         * 程序的其他地方使用了Netty影响在实例化传输客户端之前初始化处理器的数量
         ***/
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(SearchApplication.class,args);
    }
}
