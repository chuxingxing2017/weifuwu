package com.changgou.pay.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.pay.service.WeiXinPayService;
import com.github.wxpay.sdk.WXPayUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/3/4
 */
@RestController
@RequestMapping("/weixin/pay")
public class WeiXinPayController {

    @Autowired
    WeiXinPayService weiXinPayService;

    @Autowired
    private Environment env;

    @Autowired
    RabbitTemplate rabbitTemplate;
    /**
     * @Description 生成订单支付二维码
     * @Author xingge
     * @Param      * @param outtradeno   订单号
     * @param
     * @Date 21:19 2020/3/4
     * @Version 2.1
     **/
    @PostMapping("/create/native")
    public Result creatNative(@RequestParam Map<String, String> parameters) {
        Map<String, String> map = weiXinPayService.createNative(parameters);
        return new Result(true, StatusCode.OK, "创建支付二维码成功", map);
    }
    
    /**
     * @Description 查询支付状态
     * @Author xingge
     * @Param      * @param outtradeno  订单号
     * @Date 15:31 2020/3/6
     * @Version 2.1
     **/
    @RequestMapping("/status/query")
    public Result querySttus(String outtradeno) {
        Map<String, String> map = weiXinPayService.queryPayStatus(outtradeno);
        return new Result(true, StatusCode.OK, "查询支付状态成功",map);
    }

    /**
     * @Description 微信回调执行的方法
     * @Author xingge
     * @Param      * @param request
     * @Date 16:26 2020/3/6
     * @Version 2.1
     **/
    @RequestMapping("/notify/url")
    public String notifyUrl(HttpServletRequest request) throws Exception {
        //获取回调信息
        ServletInputStream inputStream = request.getInputStream();
        //网络传输字节流操作(内存操作)
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //定义缓冲区
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer,0,len);
        }
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        inputStream.close();
        //获取数据
        String strXML = new String(byteArrayOutputStream.toByteArray(), "UTF-8");
        Map<String, String> map = WXPayUtil.xmlToMap(strXML);           //回调数据

        //获取附加数据
        String attach = map.get("attach");
        Map<String,String> attachMap = JSON.parseObject(attach, Map.class);
//        System.out.println("exchange:" + env.getProperty("mq.pay.exchange.order"));
//        System.out.println("key:" + env.getProperty("mq.pay.routing.key"));

        //获取回调的mq信息
        String exchange = env.getProperty(attachMap.get("exchange"));
        String routingKey = env.getProperty(attachMap.get("routingKey"));

        // 将回调数据发送mq
//        rabbitTemplate.convertAndSend(env.getProperty("mq.pay.exchange.order"), env.getProperty("mq.pay.routing.key"), JSON.toJSONString(map));
        rabbitTemplate.convertAndSend(exchange,routingKey,JSON.toJSONString(map));

        return "success";
    }
}
