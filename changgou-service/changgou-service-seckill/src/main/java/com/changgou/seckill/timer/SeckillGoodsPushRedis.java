package com.changgou.seckill.timer;

import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.service.SeckillGoodsService;
import entity.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/3/6
 */
@Component
public class SeckillGoodsPushRedis {

    @Autowired
    SeckillGoodsService seckillGoodsService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired(required = false)
    SeckillGoodsMapper seckillGoodsMapper;

    /**
     * @Description 定时将秒杀商品加入到redis服务器
     * @Author xingge
     * @Param      * @param
     * @Date 19:25 2020/3/6
     * @Version 2.1
     **/
    @Scheduled(cron = "0/5 * * * * ?")
    public void pushGoodsToRedis() {
        //获取当前时间段之后的5个时间段
        List<Date> dateMenus = DateUtil.getDateMenus();
        //将该五个时间段的商品数据压入到redis中
        for (Date dateMenu : dateMenus) {
            //将日期转换成String类型
            String key_rule = DateUtil.data2str(dateMenu, DateUtil.PATTERN_YYYYMMDDHH);

            //查询该时间段下的商品(要求库存大于0,审核通过)   条件过滤对象
            Example example = new Example(SeckillGoods.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andGreaterThan("stockCount", 0);   //库存大于0
            criteria.andEqualTo("status", "1");         //审核状态为上线状态
            criteria.andGreaterThanOrEqualTo("startTime", dateMenu);        //起始时间
            criteria.andLessThanOrEqualTo("endTime", DateUtil.addDateHour(dateMenu, 2));    //结束时间

            //去重添加数据
            Set ids = redisTemplate.boundHashOps("SeckillGoods" + key_rule).keys();
            if (ids != null && ids.size() > 0) {
                criteria.andNotIn("id",ids);
            }
            List<SeckillGoods> seckillGoods = seckillGoodsMapper.selectByExample(example);

            //压入数据
            if (seckillGoods != null && seckillGoods.size() > 0) {
                for (SeckillGoods seckillGood : seckillGoods) {
                    //将数据押入到redis中
                    redisTemplate.boundHashOps("SeckillGoods"+key_rule).put(seckillGood.getId(),seckillGood);

                    //商品库存数量存入redis中,单独的一个数据,为了防值多线程超卖操作
                    redisTemplate.boundHashOps("SeckillGoodsCount").put(seckillGood.getId(),seckillGood.getStockCount());
                }
            }
            System.out.println("每五秒执行一次,获得数据数: "+seckillGoods.size());
        }
    }
}
