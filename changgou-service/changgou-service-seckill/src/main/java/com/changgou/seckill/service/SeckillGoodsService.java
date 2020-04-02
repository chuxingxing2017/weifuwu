package com.changgou.seckill.service;

import com.changgou.seckill.pojo.SeckillGoods;

import java.util.List;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/3/6
 */
public interface SeckillGoodsService {
    /**
     * @Description 查询秒杀商品列表
     * @Author xingge
     * @Param      * @param time
     * @Date 16:03 2020/3/7
     * @Version 2.1
     **/

    List<SeckillGoods> list(String time);

    /**
     * @Description 根据商品Id查询商品详情
     * @Author xingge
     * @Param      * @param time
     * @param id
     * @Date 16:08 2020/3/7
     * @Version 2.1
     **/

    SeckillGoods one(String time, Long id);
}
