package com.changgou.order.controller;

import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import entity.Result;
import entity.StatusCode;
import entity.TokenDecode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName CartController
 * @Description
 * @Author 传智播客
 * @Date 14:37 2020/3/2
 * @Version 2.1
 **/
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * @author 栗子
     * @Description 商品加入购物车
     * @Date 14:41 2020/3/2
     * @param id  sku id
     * @param num  加入购物车数量
     * @return entity.Result
     **/
    @RequestMapping("/add")
    public Result add(Long id, Integer num){
//        String username = "wangwu";
        String username = TokenDecode.getUserInfo().get("username");
        cartService.add(id, num, username);
        return new Result(true, StatusCode.OK, "商品加入购物车成功");
    }

    /**
     * @author 栗子
     * @Description 获取购物车列表数据
     * @Date 15:22 2020/3/2
     * @param
     * @return entity.Result<java.util.List<com.changgou.order.pojo.OrderItem>>
     **/
    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/list")
    public Result<List<OrderItem>> list(){
//        String username = "wangwu";
        String username = TokenDecode.getUserInfo().get("username");
        List<OrderItem> list = cartService.list(username);
        return new Result<>(true, StatusCode.OK, "查询购物车列表数据成功", list);
    }


}
