package com.changgou.user.service.impl;

import com.changgou.user.dao.AddressMapper;
import com.changgou.user.pojo.Address;
import com.changgou.user.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/3/4
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressMapper addressMapper;

    @Override
    public List<Address> list(String username) {
        Address address = new Address();
        address.setUsername(username);
        return addressMapper.select(address);
    }
}
