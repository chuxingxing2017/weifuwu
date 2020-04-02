package com.changgou.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.pay.service.WeiXinPayService;
import com.github.wxpay.sdk.WXPayUtil;
import entity.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/3/4
 */
@Service
public class WeiXinPayServiceImpl implements WeiXinPayService {

    @Value("${weixin.appid}")
    private String appid;           // 微信公众账号或开放平台APP的唯一标识

    @Value("${weixin.partner}")
    private String partner;         // 商户号

    @Value("${weixin.partnerkey}")
    private String partnerkey;      // 商户密钥

    @Value("${weixin.notifyurl}")
    private String notifyurl;       // 回调地址


    /**
     * @author 栗子
     * @Description 生成支付二维码
     * @Date 22:11 2019/8/25
     * @param /out_trade_no 商户订单号
     * @param /total_fee    支付金额
     *         /队列信息
     * @return java.util.Map<java.lang.String,java.lang.String>
     **/
    @Override
    public Map<String, String> createNative(Map<String,String> parameters) {

        try {
            // 统一下单地址
            String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
            // 封装支付接口需要的数据
            Map<String, String> data = new HashMap<>();
            data.put("appid", appid);                               // 微信公众账号或开放平台APP的唯一标识
            data.put("mch_id", partner);                            // 商户号
            data.put("nonce_str", WXPayUtil.generateNonceStr());    // 随机字符串
            data.put("body", "畅购商城");                            // 商品描述
            data.put("out_trade_no", parameters.get("out_trade_no"));                 // 商户订单号
            data.put("total_fee", parameters.get("total_fee"));                       // 商品支付金额
            data.put("spbill_create_ip", "127.0.0.1");              // 终端ip
            data.put("notify_url", notifyurl);                      // 回调地址
            data.put("trade_type", "NATIVE");                       // 支付类

            // day14天：需要附加数据（交互机、路由器、用户名）
            Map<String, String> attachMap = new HashMap<>();
            attachMap.put("exchange", parameters.get("exchange"));
            attachMap.put("routingKey", parameters.get("routingKey"));
            attachMap.put("username", parameters.get("username"));
            data.put("attach", JSON.toJSONString(attachMap));

            // 将数据转成xml
            String signedXml = WXPayUtil.generateSignedXml(data, partnerkey);
            // 创建HttpClient发送请求
            HttpClient httpClient = new HttpClient(url);
            httpClient.setHttps(true);
            httpClient.setXmlParam(signedXml);
            httpClient.post();
            // 处理响应数据
            String strXML = httpClient.getContent();
            Map<String, String> map = WXPayUtil.xmlToMap(strXML);   // 将xml转成map
            map.put("out_trade_no", parameters.get("out_trade_no"));
            map.put("total_fee", parameters.get("total_fee"));
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description 查询支付状态
     * @Author xingge
     * @Param      * @param out_trade_no
     * @Date 15:25 2020/3/6
     * @Version 2.1
     **/

    @Override
    public Map<String, String> queryPayStatus(String out_trade_no) {
        try {
            // 查询订单地址
            String url = "https://api.mch.weixin.qq.com/pay/orderquery";

            //封装支付接口需要的数据
            HashMap<String, String> data = new HashMap<>();
            data.put("appid", appid);       //微信公众号或开房平台App的唯一标识
            data.put("mch_id", partner);    //商户号
            data.put("nonce_str", WXPayUtil.generateNonceStr());    // 随机字符串
            data.put("out_trade_no", out_trade_no);                 // 商户订单号

            // 将数据转成xml
            String signedXml = WXPayUtil.generateSignedXml(data, partnerkey);
            // 创建HttpClient发送请求
            HttpClient httpClient = new HttpClient(url);
            httpClient.setHttps(true);
            httpClient.setXmlParam(signedXml);
            httpClient.post();
            // 处理响应数据
            String strXML = httpClient.getContent();
            Map<String, String> map = WXPayUtil.xmlToMap(strXML);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
