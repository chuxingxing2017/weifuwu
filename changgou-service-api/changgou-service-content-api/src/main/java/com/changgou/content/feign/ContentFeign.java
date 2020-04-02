package com.changgou.content.feign;
import com.changgou.content.pojo.Content;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/****
 * @Author:传智播客
 * @Description:
 * @Date 2019/6/18 13:58
 *****/
@FeignClient(name="content")
@RequestMapping("/content")
public interface ContentFeign {

    /**
     * @author 栗子
     * @Description 根据广告分类id查询广告列表数据
     * @Date 16:05 2020/2/23
     * @param categoryId
     * @return entity.Result<java.util.List<com.changgou.content.pojo.Content>>
     **/
    @GetMapping("/list/category/{categoryId}")
    Result<List<Content>> findListByCategoryId(@PathVariable(value = "categoryId") Long categoryId);
}