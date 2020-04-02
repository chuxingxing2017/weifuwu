package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.SkuInfoMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SkuInfoService;
import entity.Result;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/25
 */
@Service
public class SkuInfoServiceImpl implements SkuInfoService {

    @Autowired //该接口主要用于索引数据操作，主要使用它来实现将数据导入到ES索引库中
    private SkuInfoMapper skuInfoMapper;

    @Autowired(required = false)  //微服务间调用 feign
    private SkuFeign skuFeign;

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    /**
     * @Description 将数据库数据导入到索引库
     * @Author xingge
     * @Param      * @param 
     * @Date 19:03 2020/2/25
     * @Version 2.1
     **/
    @Override
    public void importSkuInfoToEs() {
        //通过feign调用商品服务接口
        Result<List<Sku>> result = skuFeign.findSkuByStatus("1");
        String text = JSON.toJSONString(result.getData());
        //将数据转成SkuInfo
        List<SkuInfo> skuInfos = JSON.parseArray(text, SkuInfo.class);
        //处理动态字段
        for (SkuInfo skuInfo : skuInfos) {
            String spec = skuInfo.getSpec();
            Map specmap = JSON.parseObject(spec, Map.class);
            skuInfo.setSpecMap(specmap);
        }
        //将数据导入到ES中
        skuInfoMapper.saveAll(skuInfos);
    }

    /**
     * @Description 检索方法
     * @Author xingge
     * @Param      * @param searchMap
     * @Date 21:20 2020/2/25
     * @Version 2.1
     **/
    @Override
    public Map<String, Object> search(Map<String, String> searchMap) {
        //构建索引条件(后期有多个条件,因此封装一个方法)
        NativeSearchQueryBuilder builder = builderBasicQuery(searchMap);
        //根据条件(关键字)搜索
        Map<String, Object> resultMap = searchForPage(builder);

//        //统计查询分类
//        List<String> categoryList = searchCategoryList(builder);
//        resultMap.put("categoryList", categoryList);
//
//        //统计品牌列表
//        List<String> brandList = searchBrandList(builder);
//        resultMap.put("brandList", brandList);
//
//        //统计规格数据
//        Map<String, Set<String>> specMap = searchSpecMap(builder);
//        resultMap.put("specMap", specMap);
        //获取所有分组结果
        Map<String,Object> map = groupList(builder);
        resultMap.putAll(map);
        //返回结果集
        return resultMap;
    }

    /**
     * @Description 综合查询方法
     * @Author xingge
     * @Param      * @param builder
     * @Date 21:33 2020/2/27
     * @Version 2.1
     **/

    private Map<String, Object> groupList(NativeSearchQueryBuilder builder) {
        builder.addAggregation(AggregationBuilders.terms("skuBrand").field("brandName").size(100));
        builder.addAggregation(AggregationBuilders.terms("skuCategory").field("categoryName").size(100));
        builder.addAggregation(AggregationBuilders.terms("skuSpec").field("spec.keyword").size(10000));
        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);
        Aggregations aggregations = aggregatedPage.getAggregations();

        // 获取分组结果集
        List<String> brandList = getList(aggregations, "skuBrand");         // 品牌分组
        List<String> categoryList = getList(aggregations, "skuCategory");   // 分类分组
        List<String> specList = getList(aggregations, "skuSpec");           // 规格分组
        Map<String, Set<String>> resultMap = pullMap(specList);

        // 封装数据
        Map<String, Object> map = new HashMap<>();
        map.put("brandList", brandList);
        map.put("categoryList", categoryList);
        map.put("specList", resultMap);
        return map;
    }

    /**
     * @Description 获取聚合查询结果集
     * @Author xingge
     * @Param      * @param aggregations
     * @param skuBrand
     * @Date 12:28 2020/2/28
     * @Version 2.1
     **/

    private List<String> getList(Aggregations aggregations, String skuBrand) {
        StringTerms stringTerms = aggregations.get(skuBrand);
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        ArrayList<String> list = new ArrayList<>();
        for (StringTerms.Bucket bucket : buckets) {
            list.add(bucket.getKeyAsString());
        }
        return list;
    }

    /**
     * @Description 构建索引条件
     * @Author xingge
     * @Param      * @param searchMap
     * @Date 21:26 2020/2/25
     * @Version 2.1
     **/

    private NativeSearchQueryBuilder builderBasicQuery(Map<String, String> searchMap) {
        //封装检索条件
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        //封装过滤条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //获取请求参数条件
        String keywords = searchMap.get("keywords");
        //根据关键过滤
        if (!StringUtils.isEmpty(keywords)) {
            builder.withQuery(QueryBuilders.matchQuery("name", keywords));
        }
        //根据商品分类过滤
        String category = searchMap.get("category");
        if (!StringUtils.isEmpty(category)) {
            boolQuery.must(QueryBuilders.matchQuery("categoryName", category));
        }
        //根据品牌过滤
        String brand = searchMap.get("brand");
        if (!StringUtils.isEmpty(brand)) {
            boolQuery.must(QueryBuilders.matchQuery("brandName", brand));
        }
        //根据规格过滤
        Set<String> keys = searchMap.keySet();
        for (String key : keys) {
            if (key.startsWith("spec_")) {
                boolQuery.must(QueryBuilders.matchQuery("specMap." + key.substring(5) + ".keyword", searchMap.get(key)));
            }
        }

        //根据价格过滤
        String price = searchMap.get("price");
        if (!StringUtils.isEmpty(price)) {
            String[] split = price.split("-");
            boolQuery.must(QueryBuilders.rangeQuery("price").gte(split[0]));
            if (split.length == 2) {
                boolQuery.must(QueryBuilders.rangeQuery("price").lte(split[1]));
            }
        }

        //结果排序
        String sortRule = searchMap.get("sortRule");     //排序的规则
        String sortField = searchMap.get("sortField");   //排序的字段
        if (!StringUtils.isEmpty(sortField)) {
            builder.withSort(SortBuilders.fieldSort(sortField).order(SortOrder.valueOf(sortRule)));
        }

        //添加过滤查询
        builder.withFilter(boolQuery);

        //分页操作
        String pageNum = searchMap.get("pageNum");
        if (StringUtils.isEmpty(pageNum)) {
            pageNum = "1";  //,默认第一页
        }
        int page = Integer.parseInt(pageNum) - 1;  //起始行 = (当前页-1)*每业显示行数
        int size = 40;      //每页显示行数
        //分页查询助手对象
        PageRequest pageable = PageRequest.of(page, size);
        builder.withPageable(pageable);  //分页查询

        return builder;
    }

    /**
     * @Description 统计规格列表
     * @Author xingge
     * @Param      * @param builder
     * @Date 17:13 2020/2/27
     * @Version 2.1
     **/

    private Map<String, Set<String>> searchSpecMap(NativeSearchQueryBuilder builder) {
        List<String> list = new ArrayList<>();
        //聚合查询
        builder.addAggregation(AggregationBuilders.terms("specMap").field("spec.keyword").size(10000));
        AggregatedPage<SkuInfo> page = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);
        //获取结果集
        Aggregations aggregations = page.getAggregations();
        list = getList(aggregations,"specMap");
        Map<String, Set<String>> map = pullMap(list);
        return map;
    }

    /**
     * @Description 处理规格数据,压入map中
     * @Author xingge
     * @Param      * @param list
     * @Date 17:20 2020/2/27
     * @Version 2.1
     **/
    private Map<String, Set<String>> pullMap(List<String> list) {
        Map<String, Set<String>> map = new HashMap<>();
        //处理规格结果集
        for (String spec : list) {
            //将json装换成map
            Map<String,String> specMap = JSON.parseObject(spec, Map.class);
            Set<Map.Entry<String, String>> entries = specMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String key = entry.getKey();        //规格
                String value = entry.getValue();    //规格选项
                Set<String> set = map.get(key);
                if (set==null) {
                    set = new HashSet<>();
                }
                set.add(value);
                map.put(key, set);
            }
        }
        return map;
    }

    /**
     * @Description 统计品牌列表
     * @Author xingge
     * @Param      * @param builder
     * @Date 16:43 2020/2/27
     * @Version 2.1
     **/
    private List<String> searchBrandList(NativeSearchQueryBuilder builder) {
        //聚合查询
        builder.addAggregation(AggregationBuilders.terms("skuBrand").field("brandName"));
        //es模板根据条件,对数据分页查询
        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);
        //获取结果集
        Aggregations aggregations = aggregatedPage.getAggregations();
        //处理结果集
        List<String> list = new ArrayList<>();
        StringTerms stringTerms = aggregations.get("skuBrand");
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            list.add(bucket.getKeyAsString());
        }
        return list;
    }

    /**
     * @Description 根据条件搜索
     * @Author xingge
     * @Param      * @param builder
     * @Date 21:25 2020/2/25
     * @Version 2.1
     **/
    private Map<String, Object> searchForPage(NativeSearchQueryBuilder builder) {

        //设置高亮条件 (实际就是给要高亮的文字,添加标签)
        HighlightBuilder.Field field = new HighlightBuilder.Field("name");
        field.preTags("<font color='red'>");  //设置前缀
        field.postTags("</font>");
        //field.fragmentSize(100);    //显示数据的字符个数
        builder.withHighlightFields(field);

        //操作结果集的匿名内部类
        SearchResultMapper searchResultMapper = new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                //封装高亮结果集
                ArrayList<T> content = new ArrayList<>();
                //获取高亮结果集
                SearchHits hits = response.getHits();
                if (hits!=null) {
                    for (SearchHit hit : hits) {
                        String result = hit.getSourceAsString();   //封装普通结果集
                        //将结果转成pojo
                        SkuInfo skuInfo = JSON.parseObject(result, SkuInfo.class);
                        HighlightField highlightField = hit.getHighlightFields().get("name");
                        if (highlightField != null) {
                            //获取高亮结果集
                            Text[] texts = highlightField.getFragments();
                            //替换普通结果
                            skuInfo.setName(texts[0].toString());
                        }
                        content.add((T) skuInfo);
                    }
                }
                return new AggregatedPageImpl<>(content, pageable, hits.getTotalHits());
            }
        };
        //生成索引条件
        NativeSearchQuery build = builder.build();
        //使用spring的es模板,根据条件进行查询,   1.索引条件  2.封装数据的pojo对象.class文件
        AggregatedPage<SkuInfo> page = elasticsearchTemplate.queryForPage(build, SkuInfo.class,searchResultMapper);
        List<SkuInfo> rows = page.getContent();         //搜索的结果集
        long totalElements = page.getTotalElements();   //总条数
        int totalPages = page.getTotalPages();          //总页数
        //创建封装数据的map集合
        Map<String, Object> map = new HashMap<>();
        //封装数据
        map.put("rows", rows);
        map.put("totalElements", totalElements);
        map.put("totalPages", totalPages);
        map.put("pageNum",build.getPageable().getPageNumber()+1);   //当前页
        map.put("pageSize", build.getPageable().getPageSize());     //每页显示条数
        return map;
    }


    /**
     * @Description 查询关键字对应的商品的所有分类
     * @Author xingge
     * @Param      * @param builder
     * @Date 16:16 2020/2/27
     * @Version 2.1
     **/
    public List<String> searchCategoryList(NativeSearchQueryBuilder builder) {
        ArrayList<String> list = new ArrayList<>();
        //查询聚合分类  查询所用商品的分类列表集合   skuCategoryGroupBy给聚合条件取别名
        String skuCategoryGroupby = "skuCategory";
        builder.addAggregation(AggregationBuilders.terms(skuCategoryGroupby).field("categoryName"));
        //执行搜索
        AggregatedPage<SkuInfo> result = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);
        //获取聚合分类结果
        Aggregations aggregations = result.getAggregations();
        //处理分组结果集
        StringTerms stringTerms = aggregations.get(skuCategoryGroupby);
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            list.add(bucket.getKeyAsString());
        }
        //返回分类名称
        return list;
    }
}
