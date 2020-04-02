package com.changgou.util;

import com.changgou.file.FastDFSFile;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description 操作DFS的工具类
 * @date 2020/2/20
 */
public class FastDFSClient {
    //首先需要连接FastDFS
    static {
        String filename = "fdfs_client.conf"; //指定附件名称
        String conf_filename = new ClassPathResource(filename).getPath();

        //需要加载FastDFS配置文件
        try {
            ClientGlobal.init(conf_filename);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }


    //接受附件信息:名称,内容,扩展名...
    /**
     * @Description 上传文件
     * @Author xingge
     * @Param      * @param fastDFSFile 文件信息 名称,内容,扩展名...
     * @Date 19:01 2020/2/23
     * @Version 2.1
     **/

    public static String[] uploadFile(FastDFSFile fastDFSFile) {
        try {
            byte[] file_buff = fastDFSFile.getContent();
            String ext_name = fastDFSFile.getExt();
            NameValuePair[] meta_list = new NameValuePair[1];
            meta_list[0] = new NameValuePair(fastDFSFile.getAuthor());
            //创建跟踪服务器客户端
            TrackerClient trackerClient = new TrackerClient();
            //获取跟踪服务器服务端
            TrackerServer trackerServer = trackerClient.getConnection();
            //创建储存服务器客户端
            StorageClient storageClient = new StorageClient(trackerServer, null);
            //文件上传
            String[] uploadResult = storageClient.upload_file(file_buff, ext_name, meta_list);
            return uploadResult;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return null;
    }
    // 文件上传的其实无需封装文件，因为在controller接收附件信息MultipartFile中就已经包含了 start
    /**
     * @author 栗子
     * @Description 文件上传
     * @Date 17:42 2019/8/11
     * @param file_buff 附件
     * @param ext_name 附件扩展名
     * @param des 附件备注
     * @return java.lang.String[]
     **/
    public static String[] uploadFile(byte[] file_buff, String ext_name, String des){

        try {
            NameValuePair[] meta_list = new NameValuePair[1];
            meta_list[0] = new NameValuePair(des);
            // 1、创建跟踪服务器客户端
            TrackerClient trackerClient = new TrackerClient();
            // 2、获取跟踪服务器
            TrackerServer trackerServer = trackerClient.getConnection();
            // 3、创建存储服务器客户端
            StorageClient storageClient = new StorageClient(trackerServer, null);
            // 4、文件上传
            String[] uploadResult = storageClient.upload_file(file_buff, ext_name, meta_list);
            return uploadResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    // 文件上传的其实无需封装文件，因为在controller接收附件信息MultipartFile中就已经包含了 end


    /**
     * @author 栗子
     * @Description 获取tracker服务器地址
     * @Date 18:05 2019/8/11
     * @param
     * @return java.lang.String
     **/
    public static String getTrackerUrl(){
        try {
            // 1、创建跟踪服务器客户端
            TrackerClient trackerClient = new TrackerClient();
            // 2、获取跟踪服务器
            TrackerServer trackerServer = trackerClient.getConnection();
            // 3、获取跟踪服务器地址
            String hostAddress = trackerServer.getInetSocketAddress().getAddress().getHostAddress();
            int port = ClientGlobal.getG_tracker_http_port();
            String url = "http://" + hostAddress + ":" + port;
            return url;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] downLoadFile(String group_name,String remote_filename){
        try {
            // 1、创建跟踪服务器客户端
            TrackerClient trackerClient = new TrackerClient();
            // 2、获取跟踪服务器
            TrackerServer trackerServer = trackerClient.getConnection();
            // 3.创建存储服务器客户端
            StorageClient storageClient = new StorageClient(trackerServer, null);
            //文件下载
            byte[] file_buff = storageClient.download_file(group_name, remote_filename);
            return file_buff;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /** 
    * @Description: 删除文件
    * @Param: [group_name,组名 remote_filename文件名]
    * @return: void 
    * @Author: Mr.Wang 
    * @Date: 2020/2/20 
    */ 
    public static void deleteFile(String group_name,String remote_filename)  {
        try {
            //创建跟踪服务器客户端
            TrackerClient trackerClient = new TrackerClient();
            //获取追踪服务器
            TrackerServer trackerServer = trackerClient.getConnection();
            //创建存储服务器客户端
            StorageClient storageClient = new StorageClient(trackerServer, null);
            //文件删除
            storageClient.delete_file(group_name, remote_filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * @Description: 获取服务器信息
    * @Param: [group_name, remote_filename]
    * @return: org.csource.fastdfs.FileInfo
    * @Author: Mr.Wang
    * @Date: 2020/2/20
    */
    public static FileInfo getFileInfo(String group_name,String remote_filename) {
        try {
            //创建跟踪服务器客户端
            TrackerClient trackerClient = new TrackerClient();
            //获取跟踪服务器
            TrackerServer trackerServer = trackerClient.getConnection();
            //创建存储服务器客户端
            StorageClient storageClient = new StorageClient(trackerServer, null);
            //获取文件信息
            FileInfo file_info = storageClient.get_file_info(group_name, remote_filename);
            return file_info;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
    * @Description: 获取分组服务器的信息
    * @Param: [group_name]
    * @return: org.csource.fastdfs.StorageServer
    * @Author: Mr.Wang
    * @Date: 2020/2/20
    */ 
    public static StorageServer getStorageServerInfo(String group_name) {
        try {
            //创建跟踪服务器客户端
            TrackerClient trackerClient = new TrackerClient();
            //获取跟踪服务器
            TrackerServer trackerServer = trackerClient.getConnection();
            //创建服务器客户端
            StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer, null);
            return storeStorage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /** 
    * @Description: 获取多个存储服务器信息
    * @Param: [groupname分组名, filename文件名]
    * @return: org.csource.fastdfs.ServerInfo[] 
    * @Author: Mr.Wang 
    * @Date: 2020/2/20 
    */ 
    public static ServerInfo[] getServerInfo(String groupname,String filename) {
        try {
            //创建跟踪服务器客户端
            TrackerClient trackerClient = new TrackerClient();
            //获取跟踪服务器
            TrackerServer trackerServer = trackerClient.getConnection();
            //获取集群下的存储服务器的信息
            ServerInfo[] fetchStorages = trackerClient.getFetchStorages(trackerServer, groupname, filename);
            return fetchStorages;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
