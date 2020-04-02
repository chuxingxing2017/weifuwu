package com.changgou.content.controller;

import com.changgou.content.pojo.Content;
import com.changgou.content.service.ContentService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/24
 */
@RestController
@RequestMapping("/content")
public class ContentController {
    @Autowired
    ContentService contentService;

    /**
     * @Description 根据分类id 查询广告信息
     * @Author xingge
     * @Param      * @param id
     * @Date 9:16 2020/3/19
     * @Version 2.1
     **/
    @GetMapping(value = "/list/category/{id}")
    public Result<List<Content>> findByCategory(@PathVariable Long id){
        //根据分类ID查询广告集合
        List<Content> contents = contentService.findByCategory(id);
        return new Result<List<Content>>(true, StatusCode.OK,"查询成功！",contents);
    }
}
