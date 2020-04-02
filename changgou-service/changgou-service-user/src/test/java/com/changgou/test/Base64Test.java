package com.changgou.test;

import org.junit.Test;

import java.util.Base64;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/29
 */
public class Base64Test {

    @Test
    public void demo() throws Exception{
        String url = "www.itheima.com";
        //编码
        byte[] encode = Base64.getEncoder().encode(url.getBytes("UTF-8"));
        String encodeUrl = new String(encode, "UTF-8");
        System.out.println(encodeUrl);
        //解码
        byte[] decode = Base64.getDecoder().decode(encode);
        String decodeUrl = new String(decode, "UTF-8");
        System.out.println(decodeUrl);
    }
}
