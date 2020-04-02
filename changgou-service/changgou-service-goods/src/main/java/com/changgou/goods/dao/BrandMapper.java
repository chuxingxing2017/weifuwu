package com.changgou.goods.dao;

import com.changgou.goods.pojo.Brand;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/19
 */
@Component
public interface BrandMapper extends Mapper<Brand>{

    /**
     * @Description 根据三级分类的id查询分类对应的品牌集合
     * @Author xingge
     * @Param      * @param categoryid
     * @Date 18:36 2020/2/21
     * @Version 2.1
     **/
    @Select("SELECT tb.* FROM tb_brand tb,tb_category_brand tbc where tbc.category_id = #{categoryid} and tb.id=tbc.brand_id")
    List<Brand> findByCategory(Integer categoryid);
}
