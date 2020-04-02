package com.changgou.pay;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/3/4
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
public class WeiXinPayApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeiXinPayApplication.class, args);
    }

    @Autowired
    private Environment env;
    // 创建队列
    @Bean
    public Queue orderQueue(){
        return new Queue(env.getProperty("mq.pay.queue.order"), true);
    }

    // 创建交换机
    @Bean
    public Exchange orderExchange(){
        return new DirectExchange(env.getProperty("mq.pay.exchange.order"),true, false);
    }

    // 队列绑定到交换机
    @Bean
    public Binding bindQueueToExchange(Queue orderQueue, Exchange orderExchange){
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(env.getProperty("mq.pay.routing.key")).noargs();
    }

    //************************************秒杀订单队列************************************
    // 创建队列
    @Bean
    public Queue seckillOrderQueue(){
        return new Queue(env.getProperty("mq.pay.queue.seckillorder"), true);
    }

    // 创建交换机
    @Bean
    public Exchange seckillOrderExchange(){
        return new DirectExchange(env.getProperty("mq.pay.exchange.seckillorder"),true, false);
    }

    // 队列绑定到交换机
    @Bean
    public Binding bindQueueToExchangeForSeckill(Queue seckillOrderQueue, Exchange seckillOrderExchange){
        return BindingBuilder.bind(seckillOrderQueue).to(seckillOrderExchange)
                .with(env.getProperty("mq.pay.routing.seckillkey")).noargs();
    }
}
