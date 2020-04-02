package com.changgou.search.controller;

import com.changgou.search.service.SkuInfoService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.Map;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/25
 */
@RestController
@RequestMapping("/search")
public class SkuController {

    @Autowired
    private SkuInfoService skuInfoService;

    /**
     * @Description 将数据库的数据存储到es索引库
     * @Author xingge
     * @Param      * @param
     * @Date 21:42 2020/2/25
     * @Version 2.1
     **/

    @GetMapping("/import")
    public Result importData() {
        skuInfoService.importSkuInfoToEs();
        return new Result(true, StatusCode.OK, "s数据导入成功");
    }

    /**
     * @Description 根据条件搜索商品
     * @Author xingge
     * @Param      * @param searchMap
     * @Date 15:38 2020/2/27
     * @Version 2.1
     **/
    @GetMapping()    //RequestParam    将请求路径上的参数,封装成后面指定的数据类型中
    public Map<String,Object> search(@RequestParam(required = false) Map<String,String> searchMap) {
        Map<String, Object> resultMap = skuInfoService.search(searchMap);
        return resultMap;
    }
}
