package com.changgou.seckill.task;

import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.pojo.SeckillStatus;
import entity.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/3/7
 */
@Component
public class MultiThteadingCreateOrder {

    @Autowired(required = false)
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;


    @Async      //此方法异步执行
    public void createOrder() {

        //获取队列信息
        SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundListOps("SeckillOrderQueue").rightPop();
        try {
            String time = seckillStatus.getTime();
            Long id = seckillStatus.getGoodsId();
            String username = seckillStatus.getUsername();
            //从redis中获取订单的秒杀商品的详情
            SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps("SeckillGoods" + time).get(id);
            //SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps("SeckillGoods" + time).get(id.toString());

            //判断是否有数据,是否还有库存
            if (seckillGoods == null && seckillGoods.getStockCount() <= 0) {
                throw new RuntimeException("对不起，该商品已售罄");
            }

            //redis控制安全的扣减库存
            Long stockCount = redisTemplate.boundHashOps("SeckillGoodsCount").increment(id, -1);

            //扣减秒杀商品的库
            if (stockCount < 0) {
                //将数据同步到MySql中(定时器不会将商品写入到内存中)
                seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
                //库存小于0证明商品已售罄,将redis中的数据删除
                redisTemplate.boundHashOps("SeckillGoods"+time).delete(id);
                throw new RuntimeException("对不起，该商品已售罄");
            } else {
                //证明还有库存 ,则更新redis的商品库存
                redisTemplate.boundHashOps("SeckillGoods"+time).put(id,seckillGoods);
            }

            //还有库存,可以抢购,生成订单数据
            SeckillOrder seckillOrder = new SeckillOrder();
            seckillOrder.setId(idWorker.nextId());      //主键
            seckillOrder.setMoney(seckillGoods.getCostPrice());     //支付金额
            seckillOrder.setSeckillId(id);          //秒杀商品订单
            seckillOrder.setUserId(username);       //当前用户id
            seckillOrder.setCreateTime(new Date()); //下单时间
            seckillOrder.setStatus("0");            //支付状态

            //将订单数据加入到redis中
            redisTemplate.boundHashOps("SeckillOrder").put(username,seckillOrder);
            //扣减秒杀商品的库存(多线程不安全)
            //seckillGoods.setStockCount(seckillGoods.getStockCount()-1); //只能秒杀一件商品



            //抢单成功,更新状态信息
            seckillStatus.setStatus(2);         //排队--->支付
            seckillStatus.setOrderId(seckillOrder.getId()); //订单id
            seckillStatus.setMoney(Float.valueOf(seckillOrder.getMoney())); //订单金额

            //redis更新
            redisTemplate.boundHashOps("UserQueueStatus").put(username,seckillStatus);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
