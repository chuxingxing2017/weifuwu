package com.changgou.order.service.impl;


import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName CartServiceImpl
 * @Description
 * @Author 传智播客
 * @Date 14:38 2020/3/2
 * @Version 2.1
 **/
@Service
public class CartServiceImpl implements CartService {

    @Autowired(required = false)
    private SkuFeign skuFeign;

    @Autowired(required = false)
    private SpuFeign spuFeign;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @author 栗子
     * @Description 商品加入购物车
     * @Date 14:41 2020/3/2
     * @param id   库存id
     * @param num  购买数量
     * @param username 用户名
     * @return void
     **/
    @Override
    public void add(Long id, Integer num, String username) {
        // 获取sku
        Sku sku = skuFeign.findById(id).getData();
        if (sku != null){
            // 获取spu
            Spu spu = spuFeign.findById(sku.getSpuId()).getData();
            // 创建购物车对象并且封装数据
            OrderItem orderItem = pullGoods2OrderItem(sku, spu, num);
            // 将购物车保存到redis中
            redisTemplate.boundHashOps("cart_" + username).put(id, orderItem);
        }
    }

    /**
     * @author 栗子
     * @Description 获取购物车列表数据
     * @Date 15:20 2020/3/2
     * @param username
     * @return java.util.List<com.changgou.order.pojo.OrderItem>
     **/
    @Override
    public List<OrderItem> list(String username) {
        List<OrderItem> list = redisTemplate.boundHashOps("cart_" + username).values();
        return list;
    }

    // 将数据封装到购物车对象中
    private OrderItem pullGoods2OrderItem(Sku sku, Spu spu, Integer num) {
        OrderItem orderItem = new OrderItem();
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        orderItem.setSpuId(spu.getId());
        orderItem.setSkuId(sku.getId());
        orderItem.setName(sku.getName());                   // 商品名称
        orderItem.setPrice(sku.getPrice());                 // 商品单价
        orderItem.setNum(num);                              // 购买数量
        orderItem.setMoney(sku.getPrice() * num);           // 商品的总金额
        orderItem.setPayMoney(sku.getPrice() * num - 0);    // 实付金额 = 总金额 - 优惠金额（优惠券、京豆、e卡等）
        orderItem.setImage(sku.getImage());                 // 商品图片
        orderItem.setWeight(sku.getWeight());               // 商品毛重
        orderItem.setPostFee(0);                            // 商品的运费 = 判断本次总金额是否超过99.0元/金牌会员
        orderItem.setIsReturn("0");
        return orderItem;
    }
}
