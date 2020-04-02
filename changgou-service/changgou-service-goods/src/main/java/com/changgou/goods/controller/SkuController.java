package com.changgou.goods.controller;

import com.changgou.goods.pojo.Sku;
import com.changgou.goods.service.SkuService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/sku")
@CrossOrigin
public class SkuController {

    @Autowired
    private SkuService skuService;

    /**
     * @author 栗子
     * @Description 扣减库存
     * @Date 14:23 2020/3/3
     * @param username
     * @return entity.Result
     **/
    @GetMapping("/decrCount/{username}")
    public Result decrCount(@PathVariable(value = "username") String username){
        skuService.decrCount(username);
        return new Result(true, StatusCode.OK, "扣减库存成功");
    }

    @GetMapping("/{id}")
    public Result<Sku> findById(@PathVariable(name = "id") Long id) {
        Sku sku = skuService.findById(id);
        return new Result<>(true, StatusCode.OK, "查询Sku成功", sku);
    }



    /**
     * @Description 将正常状态的库存信息保存到索引库
     * @Author xingge
     * @Param      * @param status
     * @Date 16:27 2020/2/25
     * @Version 2.1
     **/
    @GetMapping("/findSkuByStatus/{status}")
    public Result<List<Sku>> findSkusByStatus(@PathVariable(value = "status") String status){
        List<Sku> list = skuService.findSkusByStatus(status);
        return new Result<>(true, StatusCode.OK, "查询成功", list);
    }
    /***
     * Sku分页条件搜索实现
     * @param sku
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false) Sku sku, @PathVariable  int page, @PathVariable  int size){
        //调用SkuService实现分页条件查询Sku
        PageInfo<Sku> pageInfo = skuService.findPage(sku, page, size);
        return new Result(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * Sku分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        //调用SkuService实现分页查询Sku
        PageInfo<Sku> pageInfo = skuService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * 多条件搜索品牌数据
     * @param sku
     * @return
     */
    @PostMapping(value = "/search" )
    public Result<List<Sku>> findList(@RequestBody(required = false) Sku sku){
        //调用SkuService实现条件查询Sku
        List<Sku> list = skuService.findList(sku);
        return new Result<List<Sku>>(true,StatusCode.OK,"查询成功",list);
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable Long id){
        //调用SkuService实现根据主键删除
        skuService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 修改Sku数据
     * @param sku
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    public Result update(@RequestBody Sku sku, @PathVariable Long id){
        //设置主键值
        sku.setId(id);
        //调用SkuService实现修改Sku
        skuService.update(sku);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /***
     * 新增Sku数据
     * @param sku
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Sku sku){
        //调用SkuService实现添加Sku
        skuService.add(sku);
        return new Result(true,StatusCode.OK,"添加成功");
    }



    /***
     * 查询Sku全部数据
     * @return
     */
    @GetMapping
    public Result<List<Sku>> findAll(){
        //调用SkuService实现查询所有Sku
        List<Sku> list = skuService.findAll();
        return new Result<List<Sku>>(true, StatusCode.OK,"查询成功",list) ;
    }
}
