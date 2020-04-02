package com.changgou.order.service;

import com.changgou.order.pojo.Order;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/****
 * @Author:传智播客
 * @Description:Order业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface OrderService {

    /**
     * @Description 删除订单数据  (支付失败)
     * @Author xingge
     * @Param      * @param
     * @Date 16:08 2020/3/6
     * @Version 2.1
     **/
    void deleteOrder(String orderId);
    
    /**
     * @Description  修改订单状态
     * @Author xingge
     * @Param      * @param orderId     订单id
     * @param transactionId           第三方生成的交易流水号
     * @Date 15:58 2020/3/6
     * @Version 2.1
     **/
    
    void updateStatus(String orderId , String transactionId);

    /***
     * Order多条件分页查询
     * @param order
     * @param page
     * @param size
     * @return
     */
    PageInfo<Order> findPage(Order order, int page, int size);

    /***
     * Order分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Order> findPage(int page, int size);

    /***
     * Order多条件搜索方法
     * @param order
     * @return
     */
    List<Order> findList(Order order);

    /***
     * 删除Order
     * @param id
     */
    void delete(String id);

    /***
     * 修改Order数据
     * @param order
     */
    void update(Order order);

    /***
     * 新增Order
     * @param order
     */
    Order add(Order order, HttpServletRequest request);

    /**
     * 根据ID查询Order
     * @param id
     * @return
     */
     Order findById(String id);

    /***
     * 查询所有Order
     * @return
     */
    List<Order> findAll();
}
