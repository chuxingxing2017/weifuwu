package com.changgou.goods.dao;
import com.changgou.goods.pojo.Sku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

/****
 * @Author:admin
 * @Description:Sku的Dao
 * @Date 2019/6/14 0:12
 *****/
@Component
public interface SkuMapper extends Mapper<Sku> {

    /**
     * @author 栗子
     * @Description 扣减库存
     * @Date 14:13 2020/3/3
     * @param id
     * @param num
     * @return int 影响的行数
     **/
    @Update("update tb_sku set num = num - #{num} where id = #{id}  and num > #{num}")
    int decrCount(@Param("id") Long id, @Param("num") Integer num);
}
