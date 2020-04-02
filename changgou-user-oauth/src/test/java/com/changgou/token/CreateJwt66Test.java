package com.changgou.token;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CreateJwt66Test
 * @Description
 * @Author 传智播客
 * @Date 18:40 2019/8/21
 * @Version 2.1
 **/
public class CreateJwt66Test {

    /***
     * 创建令牌测试
     */
    @Test
    public void testCreateToken(){
        //证书文件路径
        String key_location="changgou83.jks";
        //秘钥库密码
        String key_password="changgou83";
        //秘钥密码
        String keypwd = "changgou83";
        //秘钥别名
        String alias = "changgou83";

        //访问证书路径
        ClassPathResource resource = new ClassPathResource(key_location);

        //创建秘钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource,key_password.toCharArray());

        //读取秘钥对(公钥、私钥)
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias,keypwd.toCharArray());

        //获取私钥
        RSAPrivateKey rsaPrivate = (RSAPrivateKey) keyPair.getPrivate();

        //定义Payload
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("id", "1");
        tokenMap.put("name", "itheima");
        tokenMap.put("roles", "ROLE_VIP,ROLE_USER");

        //生成Jwt令牌
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(tokenMap), new RsaSigner(rsaPrivate));

        //取出令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }


    /***
     * 校验令牌
     */
    @Test
    public void testParseToken(){
        //令牌
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IlJPTEVfVklQLFJPTEVfVVNFUiIsIm5hbWUiOiJpdGhlaW1hIiwiaWQiOiIxIn0.G59hpt8_H9nqwTs1rYQaEBKxhiVUzeQ0wregy-X7dEx_u4hVvmIYq2O4cc3u5v7IiqUXiRARdVwjpJbvh_NoBfcDlRrM5ulAg1ZWQpRlJMvp3ggRresS2W5IBl5T2tply5Vtn_v1laS22elFTQ924W0LPCL6Mk5tj291aA2lq84rUJxV0CWTJcfMMRRMz07qbQcSocKzGGq35ZY5tufYSh2TI3euFVyC19Nl1zRCvVG-aejNP0mLv-6bGBUUsKfoRDI1aD-Bef6OuXqB5d871RsiQw26D_DGG8yWeaEKosysqJ-AXpYvBwfIWg7jzLOywSQueFCiAK3GoYuu3Y5SPA";

        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk9VuRBYxBdVmNNYGCcc8ST2osYmqCLqJTqLYQ/0T1ydm2TneE7yLqwoB2klenc3WP2rPcNVEPRgL/wNhQJYVlsVPiZPpDbNwvkzsusPuPXq8pipoKuwMEImYTlli03HxE0oy/Nb4VALUFckEueUgw/r1A1/MSj98/BlHqL5T159q8GP9AdNOrQQCfNsocW/7kB7ZZntoxklgW1k68Ma4bRWIOg/PDI8rgIUbhDy3NtoUGP78UZblsQE7voeGZ1xPesaCwRQgIYoGjqayqnkHfvkC3O9woBRCo2pmHa+xpiLFnqC+nA9VPPEGjDCkHJlVK608V5xez+onLaiwDj2DxwIDAQAB-----END PUBLIC KEY-----";


        //校验Jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));

        //获取Jwt原始内容
        String claims = jwt.getClaims();
        System.out.println(claims);
        //jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }

}
