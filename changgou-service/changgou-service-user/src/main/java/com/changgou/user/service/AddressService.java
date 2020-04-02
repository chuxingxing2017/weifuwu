package com.changgou.user.service;

import com.changgou.user.pojo.Address;

import java.util.List;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/3/4
 */
public interface AddressService {
    List<Address> list(String username);
}
