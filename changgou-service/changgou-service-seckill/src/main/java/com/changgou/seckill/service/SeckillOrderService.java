package com.changgou.seckill.service;

import com.changgou.seckill.pojo.SeckillStatus;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/3/6
 */
public interface SeckillOrderService {
    /**
     * @Description 根据商品id和时间段添加订单数据
     * @Author xingge
     * @Param      * @param time
     * @param id
     * @Date 16:18 2020/3/7
     * @Version 2.1
     **/
    void add(String time, Long id, String username);

    /**
     * @Description 查询订单状态
     * @Author xingge
     * @Param      * @param username
     * @Date 19:45 2020/3/7
     * @Version 2.1
     **/

    SeckillStatus queryStatus(String username);
}
