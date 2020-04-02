package com.changgou.user.service;

import com.changgou.user.pojo.User;

import java.util.List;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/29
 */
public interface UserService {
    User findById(String username);

    List<User> findAll();

    void incrUserPonits(String username, Integer point);
}
