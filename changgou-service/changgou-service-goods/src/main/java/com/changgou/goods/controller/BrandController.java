package com.changgou.goods.controller;

import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/19
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    BrandService brandService;


    @GetMapping("/category/{id}")
    public Result<Brand> findByCategory(@PathVariable("id")Integer categoryid) {
        List<Brand> brands = brandService.findByCategory(categoryid);
        return new Result<>(true, StatusCode.OK, "查询商品成功", brands);
    }
    /**
    * @Description: 查询所有品牌
    * @Param: []
    * @return: entity.Result<com.changgou.goods.pojo.Brand>
    * @Author: Mr.Wang
    * @Date: 2020/2/19
    */
    @GetMapping
    public Result<Brand> findAll() {
        List<Brand> list = brandService.findAll();
        return new Result<>(true, StatusCode.OK, "查询成功", list);
    }
    /** 
    * @Description: 根据id查询品牌 
    * @Param: [id] 
    * @return: entity.Result<com.changgou.goods.pojo.Brand> 
    * @Author: Mr.Wang 
    * @Date: 2020/2/19 
    */ 
    @GetMapping("/{id}")
    public Result<Brand> findById(@PathVariable("id") Integer id) {
        Brand brand = brandService.findById(id);
        return new Result<>(true, StatusCode.OK, "查询成功", brand);
    }

    @PostMapping
    public Result<Brand> save(@RequestBody Brand brand) {
        brandService.save(brand);
        return new Result<>(true, StatusCode.OK, "保存成功");
    }

    /**
     * @Description: 更新品牌信息
     * @Param: [brand]
     * @return: void
     * @Author: Mr.Wang
     * @Date: 2020/2/19
     */
    @PutMapping("/{id}")
    public Result<Brand> updateById(@RequestBody Brand brand, @PathVariable("id")Integer id)  {
        brand.setId(id);
        brandService.updateById(brand);
        return new Result<>(true, StatusCode.OK, "修改成功");
    }
    /** 
    * @Description: 根据id删除品牌 
    * @Param: [id] 
    * @return: entity.Result<com.changgou.goods.pojo.Brand> 
    * @Author: Mr.Wang 
    * @Date: 2020/2/19 
    */ 
    @DeleteMapping("/{id}")
    public Result<Brand> deleteById(@PathVariable("id")Integer id) {
        brandService.deleteById(id);
        return new Result<>(true, StatusCode.OK, "删除成功");
    }

    /**
     * @Description: 根据条件查询
     * 根据名称模糊查询 根据首字母查询 根据排序信息查询
     * @Param: [brand]
     * @return: java.util.List<com.changgou.goods.pojo.Brand>
     * @Author: Mr.Wang
     * @Date: 2020/2/19
     */
    @PostMapping("/search")//required   表示参数不是必须的
    public Result<Brand> findList(@RequestBody(required = false) Brand brand) {
        List<Brand> list = brandService.findList(brand);
        return new Result<>(true, StatusCode.OK, "查询成功",list);
    }

    /**
    * @Description: 分页查询
    * @Param: [page当前页数, size当前页显示条数]
    * @return: entity.Result<com.changgou.goods.pojo.Brand>
    * @Author: Mr.Wang
    * @Date: 2020/2/19
    */
    @GetMapping("/search/{page}/{size}")
    public Result<Brand> search(@PathVariable("page")Integer page,@PathVariable("size") Integer size) {
        PageInfo<Brand> pageInfo = brandService.findPage(page, size);
        return new Result<>(true, StatusCode.OK, "分页查询成功",pageInfo);
    }

    /**
     * @Description: 多条件分页查询
     * @Param: [page当前页, size当前页显示条数, brand查询条件]
     * @return: com.github.pagehelper.PageInfo<com.changgou.goods.pojo.Brand>
     * @Author: Mr.Wang
     * @Date: 2020/2/19
     */
    @PostMapping("/search/{page}/{size}")
    public Result<Brand> findPageByExample(@RequestBody Brand brand,@PathVariable("page")Integer page,@PathVariable("size")Integer size) {
        PageInfo<Brand> pageInfo = brandService.findPage(page, size, brand);
        return new Result<>(true, StatusCode.OK, "分页查询成功",pageInfo);
    }
}
