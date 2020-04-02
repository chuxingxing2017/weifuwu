package com.changgou.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/28
 */
@FeignClient(name = "search")
@RequestMapping("/search")
public interface SkuInfoFeign {

    /**
     * @Description 检索方法
     * @Author xingge
     * @Param      * @param searchMap
     * @Date 16:44 2020/2/28
     * @Version 2.1
     **/
    @GetMapping
    Map<String, Object> search(@RequestParam(required = false) Map<String, String> searchMap);
}
