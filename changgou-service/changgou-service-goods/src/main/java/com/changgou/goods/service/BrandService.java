package com.changgou.goods.service;

import com.changgou.goods.pojo.Brand;
import com.github.pagehelper.PageInfo;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/19
 */
public interface BrandService {

    /**
     * @Description 根据三级分类查询品牌信息
     * @Author xingge
     * @Param      * @param categoryid
     * @Date 18:44 2020/2/21
     * @Version 2.1
     **/

    List<Brand> findByCategory(Integer categoryid);

    /**
    * @Description: 查询所有品牌
    * @Param: []
    * @return: java.util.List<com.changgou.goods.pojo.Brand>
    * @Author: Mr.Wang
    * @Date: 2020/2/19
    */
    List<Brand> findAll();
    /**
    * @Description: 根据ID查询品牌
    * @Param: [id]
    * @return: com.changgou.goods.pojo.Brand
    * @Author: Mr.Wang
    * @Date: 2020/2/19
    */
    Brand findById(Integer id);
    /**
    * @Description: 保存品牌信息
    * @Param: [brand]
    * @return: void
    * @Author: Mr.Wang
    * @Date: 2020/2/19
    */
    void save(Brand brand);
    /**
    * @Description: 更改品牌信息
    * @Param: [brand]
    * @return: void
    * @Author: Mr.Wang
    * @Date: 2020/2/19
    */

    void updateById(Brand brand);

    /**
    * @Description: 根据id删除品牌
    * @Param: [id]
    * @return: void
    * @Author: Mr.Wang
    * @Date: 2020/2/19
    */
    void deleteById(Integer id);

    /**
    * @Description: 根据条件查询
    * @Param: [brand]
    * @return: java.util.List<com.changgou.goods.pojo.Brand>
    * @Author: Mr.Wang
    * @Date: 2020/2/19
    */
    List<Brand> findList(Brand brand);

    /** 
    * @Description: 分页查询 
    * @Param: [page当前页码, size每页显示的条数]
    * @return: PageInfo<Brand> 
    * @Author: Mr.Wang 
    * @Date: 2020/2/19 
    */ 
    PageInfo<Brand> findPage(Integer page, Integer size);

    /**
    * @Description: 多条件分页查询
    * @Param: [page当前页, size当前页显示条数, brand查询条件]
    * @return: com.github.pagehelper.PageInfo<com.changgou.goods.pojo.Brand>
    * @Author: Mr.Wang
    * @Date: 2020/2/19
    */
    PageInfo<Brand> findPage(int page, int size, Brand brand);
}
