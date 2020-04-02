package com.changgou.seckill.service.impl;

import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.service.SeckillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/3/6
 */
@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * @Description 查询秒杀商品列表
     * @Author xingge
     * @Param      * @param time
     * @Date 16:03 2020/3/7
     * @Version 2.1
     **/
    @Override
    public List<SeckillGoods> list(String time) {
        List list = redisTemplate.boundHashOps("SeckillGoods" + time).values();
        return list;
    }

    /**
     * @Description 根据商品Id查询商品详情
     * @Author xingge
     * @Param      * @param time
     * @param id
     * @Date 16:08 2020/3/7
     * @Version 2.1
     **/
    @Override
    public SeckillGoods one(String time, Long id) {
        return (SeckillGoods) redisTemplate.boundHashOps("SeckillGoods" + time).get(id);
    }

}
