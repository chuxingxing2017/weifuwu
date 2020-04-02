package com.changgou.pay.service;

import java.util.Map;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/3/4
 */
public interface WeiXinPayService {
    /**
     * @Description 创建二维码
     * @Author xingge
     * @Param      * @param outtradeno
     * @param
     * @Date 15:23 2020/3/6
     * @Version 2.1
     **/

    Map<String,String> createNative(Map<String,String> parameters);

    /**
     * @Description 查询支付状态
     * @Author xingge
     * @Param * @param null
     * @Date 15:24 2020/3/6
     * @Version 2.1
     **/
    public Map<String, String> queryPayStatus(String out_trade_no);
}
