package com.changgou.user.dao;

import com.changgou.user.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/29
 */
@Component
public interface UserMapper extends Mapper<User>{
    /**
     * @author 栗子
     * @Description 增加用户积分
     * @Date 14:26 2020/3/3
     * @param username
     * @param point
     * @return int
     **/
    @Update("UPDATE tb_user SET points = points + #{point} WHERE username = #{username}")
    int incrUserPoints(@Param("username") String username, @Param("point") Integer point);
}
