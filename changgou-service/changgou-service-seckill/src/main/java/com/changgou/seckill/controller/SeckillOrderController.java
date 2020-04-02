package com.changgou.seckill.controller;

import com.changgou.seckill.pojo.SeckillStatus;
import com.changgou.seckill.service.SeckillOrderService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/3/7
 */
@RestController
@RequestMapping("/seckill/order")
@CrossOrigin    //开启跨域访问
public class SeckillOrderController {

    @Autowired
    SeckillOrderService seckillOrderService;

    /**
     * @Description 根据商品id和时间段添加订单数据
     * @Author xingge
     * @Param      * @param time
     * @param id
     * @Date 16:18 2020/3/7
     * @Version 2.1
     **/
    @RequestMapping("/add")
    public Result add(String time,Long id) {
        try {
            String username = "zhangsan";   //登陆后可以从TokenDecode获取
            seckillOrderService.add(time,id,username);
            return new Result(true, StatusCode.OK, "提交秒杀订单成功");
        } catch (Exception e) {
            e.printStackTrace();

            return new Result(false, StatusCode.NOTFOUNDERROR, e.getMessage());
        }
    }

    @RequestMapping("/queryStatus")
    public Result queryStatus() {
        String useranme = "zhangsan";
        SeckillStatus seckillStatus =  seckillOrderService.queryStatus(useranme);
        return new Result(true, StatusCode.OK, "抢购状态",seckillStatus);
    }
}
