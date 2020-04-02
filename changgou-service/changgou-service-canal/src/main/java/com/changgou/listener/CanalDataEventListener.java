package com.changgou.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.changgou.content.feign.ContentFeign;
import com.changgou.content.pojo.Content;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.InsertListenPoint;
import com.xpand.starter.canal.annotation.ListenPoint;
import com.xpand.starter.canal.annotation.UpdateListenPoint;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/24
 */
@CanalEventListener  //Canal 监听类
public class CanalDataEventListener {

    @Autowired(required = false)
    private ContentFeign contentFeign;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * @Description 监控mysql插入类型的数据改动
     * @Author xingge
     * @Param      * @param entryType监控对数据库操作的事件
     * @param rowData 操作的行数据
     * @Date 16:56 2020/2/24
     * @Version 2.1
     **/
    
//    @InsertListenPoint
//    public void onEnventInsert(CanalEntry.EntryType entryType ,CanalEntry.RowData rowData) {
//        //获取单行列数据的集合  插入后的数据
//        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
//        //遍历集合,获取每一列的key value
//        for (CanalEntry.Column column : afterColumnsList) {
//            String name = column.getName();  //列名字
//            String value = column.getValue();//列对应的值
//            System.out.println("列名: "+name+"列值: " +value);
//        }
//    }
//    /**
//     * @Description 监控mysql更新操作类型的数据改动
//     * @Author xingge
//     * @Param      * @param entryType监控对数据库操作的事件
//     * @param rowData 操作的行数据
//     * @Date 16:56 2020/2/24
//     * @Version 2.1
//     **/
//    @UpdateListenPoint
//    public void onEnventUpdate(CanalEntry.EntryType entryType ,CanalEntry.RowData rowData) {
//        //获得更新前数据
//        List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
//        //遍历
//        for (CanalEntry.Column column : beforeColumnsList) {
//            String name = column.getName();//列名字
//            String value = column.getValue();//列对应的值
//            System.out.println("列名: "+ name+"列的值: "+value);
//        }
//        System.out.println("========更新后的数据==========");
//        //获取更新后的数据
//        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
//        for (CanalEntry.Column column : afterColumnsList) {
//            String name = column.getName();
//            String value = column.getValue();
//            System.out.println("列名: "+name+"列的值: "+value);
//        }
//    }
//
//    /**
//     * @Description 监控mysql删除类型的数据改动
//     * @Author xingge
//     * @Param      * @param entryType监控对数据库操作的事件
//     * @param rowData 操作的行数据
//     * @Date 16:56 2020/2/24
//     * @Version 2.1
//     **/
//    @UpdateListenPoint
//    public void onEnventDelete(CanalEntry.EntryType entryType ,CanalEntry.RowData rowData) {
//        //获得更新前数据
//        List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
//        //遍历
//        for (CanalEntry.Column column : beforeColumnsList) {
//            String name = column.getName();//列名字
//            String value = column.getValue();//列对应的值
//            System.out.println("列名: "+ name+"列的值: "+value);
//        }
//    }

    // 自定义监听器
    @ListenPoint(destination = "example", schema = "changgou_content", table = {"tb_content"},
            eventType = {CanalEntry.EventType.INSERT, CanalEntry.EventType.UPDATE})
    public void onEventContent(CanalEntry.EntryType entryType, CanalEntry.RowData rowData){
        // 获取分类id
        String categoryId = getColumnValue(rowData, "category_id");
        // 通过分类id查询广告列表
        Result<List<Content>> result = contentFeign.findListByCategoryId(Long.parseLong(categoryId));
        // 存入redis
        stringRedisTemplate.boundValueOps("content_" + categoryId).set(JSON.toJSONString(result.getData()));
    }

    // 获取对应列的值
    private String getColumnValue(CanalEntry.RowData rowData, String columnName) {
        List<CanalEntry.Column> list = rowData.getAfterColumnsList();
        for (CanalEntry.Column column : list) {
            if (columnName.equals(column.getName())){
                return column.getValue();
            }
        }
        return null;
    }
}
