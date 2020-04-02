package com.changgou.goods.service.impl;

import com.changgou.goods.dao.BrandMapper;
import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/19
 */
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    BrandMapper brandMapper;
    /**
     * @Description 根据三级分类查询品牌信息
     * @Author xingge
     * @Param      * @param categoryid
     * @Date 18:44 2020/2/21
     * @Version 2.1
     **/
    @Override
    public List<Brand> findByCategory(Integer categoryid) {
        return brandMapper.findByCategory(categoryid);
    }

    /**
    * @Description: 查询所有品牌
    * @Param: []
    * @return: java.util.List<com.changgou.goods.pojo.Brand>
    * @Author: Mr.Wang
    * @Date: 2020/2/19
    */
    @Override
    public List<Brand> findAll() {
        return brandMapper.selectAll();
    }
    /**
    * @Description: 根据id查询品牌
    * @Param: [id]
    * @return: com.changgou.goods.pojo.Brand
    * @Author: Mr.Wang
    * @Date: 2020/2/19
    */
    @Override
    public Brand findById(Integer id) {
        return brandMapper.selectByPrimaryKey(id);
    }
    /**
    * @Description: 保存品牌信息
    * @Param: [brand]
    * @return: void
    * @Author: Mr.Wang
    * @Date: 2020/2/19
    */
    @Override
    public void save(Brand brand) {
        brandMapper.insertSelective(brand);
    }
    /**
    * @Description: 更新品牌信息
    * @Param: [brand]
    * @return: void
    * @Author: Mr.Wang
    * @Date: 2020/2/19
    */
    @Override
    public void updateById(Brand brand) {
        brandMapper.updateByPrimaryKeySelective(brand);
    }

    /**
    * @Description: 根据id删除品牌
    * @Param: [id]
    * @return: void
    * @Author: Mr.Wang
    * @Date: 2020/2/19
    */
    @Override
    public void deleteById(Integer id) {
        brandMapper.deleteByPrimaryKey(id);

    }
    /** 
    * @Description: 根据条件查询
    * 根据名称模糊查询 根据首字母查询 根据排序信息查询
    * @Param: [brand] 
    * @return: java.util.List<com.changgou.goods.pojo.Brand> 
    * @Author: Mr.Wang 
    * @Date: 2020/2/19 
    */ 
    @Override
    public List<Brand> findList(Brand brand) {
        Example example = creatExample(brand);
        List<Brand> brands = brandMapper.selectByExample(example);
        return brands;
    }
    /**
     * @Description: 分页查询
     * @Param: [page当前页码, size每页显示的条数]
     * @return: PageInfo<Brand>
     * @Author: Mr.Wang
     * @Date: 2020/2/19
     */
    @Override
    public PageInfo<Brand> findPage(Integer page, Integer size) {
        //分页条件
        PageHelper.startPage(page, size);
        List<Brand> brands = brandMapper.selectAll();
        return new PageInfo<Brand>(brands);
    }

    /**
     * @Description: 多条件分页查询
     * @Param: [page当前页, size当前页显示条数, brand查询条件]
     * @return: com.github.pagehelper.PageInfo<com.changgou.goods.pojo.Brand>
     * @Author: Mr.Wang
     * @Date: 2020/2/19
     */
    @Override
    public PageInfo<Brand> findPage(int page, int size, Brand brand) {
        //分页查询条件
        PageHelper.startPage(page, size);
        //多体条件查询条件
        Example example = creatExample(brand);
        //查询
        List<Brand> brands = brandMapper.selectByExample(example);
        //返回结果集
        return new PageInfo<Brand>(brands);
    }

    /**
    * @Description: 创建动态查询条件
    * 根据名称模糊查询 根据首字母查询 根据排序信息查询
    * @Param: [brand]
    * @return: tk.mybatis.mapper.entity.Example
    * @Author: Mr.Wang
    * @Date: 2020/2/19
    */
    public Example creatExample(Brand brand) {
        Example example = new Example(Brand.class);
        if (brand != null) {
            //封装查询条件
            Example.Criteria criteria = example.createCriteria();
            //根据名称模糊查询
            if (!StringUtils.isEmpty(brand.getName())) {
                criteria.andLike("name", "%" + brand.getName() + "%");
            }
            //根据首字母查询
            if (!StringUtils.isEmpty(brand.getLetter())) {
                criteria.andEqualTo("letter", brand.getLetter());
            }
            //根据排序信息查询
            if (!StringUtils.isEmpty(brand.getSeq())) {
                criteria.andEqualTo("seq", brand.getSeq());
            }
        }
        return example;
    }
}
