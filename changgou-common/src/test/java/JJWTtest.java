import io.jsonwebtoken.*;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/29
 */
public class JJWTtest {

    /**
     * @Description 生成token
     * @Author xingge
     * @Param      * @param
     * @Date 16:30 2020/2/29
     * @Version 2.1
     **/
    @Test
    public void creatToken(){
        //创建JWT
        JwtBuilder builder = Jwts.builder();
        //构建头部
        HashMap<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("ketId", "JWT");
        builder.setHeader(map);
        //构建载荷
        builder.setId("001");
        builder.setIssuer("张三");
        builder.setIssuedAt(new Date());
        //构建签名
        builder.signWith(SignatureAlgorithm.HS256, "itheima");

        //生成token
        String token = builder.compact();
        System.out.println(token);
    }

    /**
     * @Description 解析token
     * @Author xingge
     * @Param      * @param
     * @Date 16:31 2020/2/29
     * @Version 2.1
     **/
    @Test
    public void parserToken(){
        String token = "eyJrZXRJZCI6IkpXVCIsImFsZyI6IkhTMjU2In0.eyJqdGkiOiIwMDEiLCJpc3MiOiLlvKDkuIkiLCJpYXQiOjE1ODI5NjQ5NzB9.nsCQNyH-Ya3GdQ2MsxSQiy0jJtiaesBEeC85m1BNuXM";
        JwtParser parser = Jwts.parser();
        parser.setSigningKey("itheima");
        Claims claims = parser.parseClaimsJws(token).getBody();
        System.out.println(claims);
    }
}
