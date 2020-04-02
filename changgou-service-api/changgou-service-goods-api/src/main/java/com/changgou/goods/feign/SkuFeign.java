package com.changgou.goods.feign;

import com.changgou.goods.pojo.Sku;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/25
 */
@FeignClient(name = "goods")
@RequestMapping("/sku")
public interface SkuFeign {

    /**
     * @Description 调用商品服务
     * @Author xingge
     * @Param      * @param status
     * @Date 17:21 2020/2/25
     * @Version 2.1
     **/
    @GetMapping("/findSkuByStatus/{status}")
    public Result<List<Sku>> findSkuByStatus(@PathVariable("status") String status);

    @GetMapping("/{id}")
    Result<Sku> findById(@PathVariable(value = "id") Long id);

    @GetMapping("/decrCount/{username}")
    Result decrCount(@PathVariable(value = "username") String username);
}
