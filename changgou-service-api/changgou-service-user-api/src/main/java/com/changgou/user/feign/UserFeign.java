package com.changgou.user.feign;

import com.changgou.user.pojo.User;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName UserFeign
 * @Description
 * @Author 传智播客
 * @Date 10:56 2020/3/2
 * @Version 2.1
 **/
@FeignClient(name = "user")
@RequestMapping("/user")
public interface UserFeign {

    /**
     * @author 栗子
     * @Description 通过用户名获取用户信息
     * @Date 10:58 2020/3/2
     * @param id
     * @return entity.Result<com.changgou.user.pojo.User>
     **/
    @GetMapping("/{id}")
    Result<User> findById(@PathVariable(name = "id") String id);

    @GetMapping("/incrUserPoints/{username}/{point}")
    Result incrUserPoints(@PathVariable(value = "username") String username,
                          @PathVariable(value = "point") Integer point);
}
