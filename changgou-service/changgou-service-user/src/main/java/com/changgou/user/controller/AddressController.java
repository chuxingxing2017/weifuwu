package com.changgou.user.controller;

import com.changgou.user.pojo.Address;
import com.changgou.user.service.AddressService;
import com.netflix.discovery.converters.Auto;
import entity.Result;
import entity.StatusCode;
import entity.TokenDecode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/3/4
 */
@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    AddressService addressService;

    /**
     * @Description 获取用户收获地址信息
     * @Author xingge
     * @Param      * @param
     * @Date 9:52 2020/3/14
     * @Version 2.1
     **/
    @GetMapping("/user/list")
    public Result list() {
        //获取请求中的用户信息(cook)
        String username = TokenDecode.getUserInfo().get("username");
        List<Address> list = addressService.list(username);
        return new Result(true, StatusCode.OK,"查询地址成功",list);
    }
}
