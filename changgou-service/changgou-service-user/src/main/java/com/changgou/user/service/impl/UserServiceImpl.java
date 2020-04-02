package com.changgou.user.service.impl;

import com.changgou.user.dao.UserMapper;
import com.changgou.user.pojo.User;
import com.changgou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/29
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;
    @Override
    public User findById(String username) {
        return userMapper.selectByPrimaryKey(username);
    }

    @Override
    public List<User> findAll() {
        return userMapper.selectAll();
    }

    @Override
    public void incrUserPonits(String username, Integer point) {
        userMapper.incrUserPoints(username, point);
    }

}
