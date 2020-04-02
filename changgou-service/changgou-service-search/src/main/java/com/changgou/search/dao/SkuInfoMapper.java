package com.changgou.search.dao;

import com.changgou.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/25
 */
//该接口主要用于索引数据操作，主要使用它来实现将数据导入到ES索引库中
public interface SkuInfoMapper extends ElasticsearchRepository<SkuInfo,Integer>{//1.实体类型  2.主键类型
}
