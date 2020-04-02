package com.changgou.order.service;

import com.changgou.order.pojo.OrderItem;

import java.util.List;

/**
 * @ClassName CartService
 * @Description
 * @Author 传智播客
 * @Date 14:38 2020/3/2
 * @Version 2.1
 **/
public interface CartService {

    /**
     * @author 栗子
     * @Description 商品加入购物车
     * @Date 14:41 2020/3/2
     * @param id   库存id
     * @param num  购买数量
     * @param username 用户名
     * @return void
     **/
    void add(Long id, Integer num, String username);

    /**
     * @author 栗子
     * @Description 获取购物车列表数据
     * @Date 15:20 2020/3/2
     * @param username
     * @return java.util.List<com.changgou.order.pojo.OrderItem>
     **/
    List<OrderItem> list(String username);
}
