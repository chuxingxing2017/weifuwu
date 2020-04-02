package com.changgou.search.service;

import java.util.Map;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/25
 */
public interface SkuInfoService {
    /**
     * @Description 将数据导入到索引库
     * @Author xingge
     * @Param      * @param
     * @Date 19:02 2020/2/25
     * @Version 2.1
     **/

    
    void importSkuInfoToEs();

    Map<String,Object> search(Map<String, String> searchMap);
}
