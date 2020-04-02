package com.changgou.search.controller;

import com.changgou.search.feign.SkuInfoFeign;
import com.changgou.search.pojo.SkuInfo;
import entity.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/28
 */
@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired(required = false)
    SkuInfoFeign skuInfoFeign;

    /**
     * @Description 搜索页面回显数据
     * @Author xingge
     * @Param      * @param 
     * @Date 16:30 2020/2/28
     * @Version 2.1
     **/
    
    @GetMapping("/list")
    public String list(@RequestParam(required = false)Map<String,String> searchMap, Model model) {
        //调用feign搜索服务
        Map<String, Object> resultMap = skuInfoFeign.search(searchMap);

        //将检索数据和返回结果翻个udaomodel域用于页面回显
        model.addAttribute("searchMap", searchMap);
        model.addAttribute("resultMap", resultMap);

        //组装URl
        String url = getUrl(searchMap);
        model.addAttribute("url", url);
        Object totalElements = resultMap.get("totalElements");
        Long total = Long.parseLong(resultMap.get("totalElements").toString());     // 总条数
        int currentpage = Integer.parseInt(resultMap.get("pageNum").toString()); // 当前页
        int pagesize = Integer.parseInt(resultMap.get("pageSize").toString());      // 每页显示的条数
        // 将分页对象设置到model域中
        Page<SkuInfo> page = new Page<>(total, currentpage, pagesize);
        model.addAttribute("page", page);
        //返回搜索页面
        return "search";
    }

    /**
     * @Description 组装URL
     * @Author xingge
     * @Param      * @param searchMap
     * @Date 17:10 2020/2/28
     * @Version 2.1
     **/
    private String getUrl(Map<String, String> searchMap) {
        String url = "/search/list";
        if (searchMap != null && searchMap.size() > 0) {
            url += "?";
            Set<Map.Entry<String, String>> entries = searchMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals("pageNum")) {
                    continue;       //前端实现分页无需拼接,跳出循环
                }
                url += key + "=" + value + "&";
            }
            url = url.substring(0,url.length() - 1);  //去掉最后一个&
        }
        return url;
    }
}
