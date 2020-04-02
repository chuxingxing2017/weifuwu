package com.changgou.seckill.controller;

import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.service.SeckillGoodsService;
import com.netflix.discovery.converters.Auto;
import entity.DateUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/3/7
 */
@RestController
@CrossOrigin        //跨域访问
@RequestMapping("/seckill/goods")
public class SeckillGoodsController {

    @Autowired
    SeckillGoodsService seckillGoodsService;

    /**
     * @Description 获取时间列表菜单
     * @Author xingge
     * @Param * @param
     * @Date 15:59 2020/3/7
     * @Version 2.1
     **/
    @RequestMapping("/menus")
    public List<Date> dateMenus() {
        return DateUtil.getDateMenus();
    }

    /**
     * @Description 查询秒杀商品列表
     * @Author xingge
     * @Param      * @param time
     * @Date 16:03 2020/3/7
     * @Version 2.1
     **/

    @RequestMapping("/list")
    public Result<SeckillGoods> list(String time) {
        List<SeckillGoods> list = seckillGoodsService.list(time);
        return new Result<>(true, StatusCode.OK, "查询秒杀商品成功", list);
    }

    /**
     * @Description 根据商品Id查询商品详情
     * @Author xingge
     * @Param      * @param time
     * @param id
     * @Date 16:08 2020/3/7
     * @Version 2.1
     **/
    @RequestMapping("/one")
    public Result one(String time,Long id) {
        SeckillGoods seckillGoods = seckillGoodsService.one(time, id);
        return new Result(true, StatusCode.OK, "查询商品详情成功", seckillGoods);
    }

}
