package com.changgou.seckill.service.impl;

import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.dao.SeckillOrderMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.pojo.SeckillStatus;
import com.changgou.seckill.service.SeckillGoodsService;
import com.changgou.seckill.service.SeckillOrderService;
import com.changgou.seckill.task.MultiThteadingCreateOrder;
import entity.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/3/6
 */
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {
    @Autowired
    RedisTemplate redisTemplate;


    @Autowired
    IdWorker idWorker;


    @Autowired(required = false)
    SeckillOrderMapper seckillOrderMapper;

    @Autowired(required = false)
    SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    MultiThteadingCreateOrder multiThteadingCreateOrder;
    /**
     * @Description
     * @Author xingge
     * @Param      * @param time   秒杀商品redis中的key
     * @param id        秒杀商品的id
     * @param username     用户id
     * @Date 16:22 2020/3/7
     * @Version 2.1
     **/
    @Override
    public void add(String time, Long id, String username) {
        ///登记用户购买的商品
        Long userQueueCount = redisTemplate.boundHashOps("UserQueueCount" + username).increment(id, 1);
        if ( userQueueCount > 1) {
            throw new RuntimeException("重复下单了...");
        }

        //将用户信息封装队列中,redis队列单线程,排序
        SeckillStatus seckillStatus = new SeckillStatus(username, new Date(), 1, id, time);
        redisTemplate.boundListOps("SeckillOrderQueue").leftPush(seckillStatus);
        //将用户的抢单信息存入到reids中
        redisTemplate.boundHashOps("UserQueueStatus").put(username,seckillStatus);

        //异步多线程执行下作单操
        multiThteadingCreateOrder.createOrder();

        //TODO
    }

    /**
     * @Description 查询订单状态
     * @Author xingge
     * @Param      * @param username
     * @Date 19:45 2020/3/7
     * @Version 2.1
     **/
    @Override
    public SeckillStatus queryStatus(String username) {
        SeckillStatus userQueueStatus = (SeckillStatus) redisTemplate.boundHashOps("UserQueueStatus").get(username);
        return userQueueStatus;
    }
}
