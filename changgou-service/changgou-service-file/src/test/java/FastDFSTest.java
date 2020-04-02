import com.changgou.util.FastDFSClient;
import org.apache.commons.io.IOUtils;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.ServerInfo;
import org.csource.fastdfs.StorageServer;
import org.junit.Test;

import java.io.FileOutputStream;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/20
 */
public class FastDFSTest {
    /** 
    * @Description: 下载文件
    * @Param: [] 
    * @return: void 
    * @Author: Mr.Wang 
    * @Date: 2020/2/20 
    */
    @Test
    public void downLoad() throws Exception{
        String group_name = "group1";
        String remote_filename = "M00/00/00/wKjThF5OPkuADtmZAADvuF4tGRU629.jpg";
        byte[] bytes = FastDFSClient.downLoadFile(group_name, remote_filename);
        IOUtils.write(bytes,new FileOutputStream("d:/1.jpg"));
    }

    @Test
    public void deleteFile() {
        String group_name = "group1";
        String remote_filename = "M00/00/00/wKjThF5OPkuADtmZAADvuF4tGRU629.jpg";
        FastDFSClient.deleteFile(group_name,remote_filename);
    }

    @Test
    public void getFileInfo() {
        String group_name = "group1";
        String remote_filename = "M00/00/00/wKjThF5OTHiAMQ04AADvuF4tGRU251.jpg";
        FileInfo fileInfo = FastDFSClient.getFileInfo(group_name, remote_filename);
        System.out.println("存储服务器地址: " + fileInfo.getCreateTimestamp());
        System.out.println("文件大小: " + fileInfo.getFileSize());
        System.out.println("文件服务器所在位置: " + fileInfo.getSourceIpAddr());
    }

    @Test
    public void getFileGroupInfo() {
        StorageServer group1 = FastDFSClient.getStorageServerInfo("group1");
        System.out.println("存储服务器地址: " + group1.getInetSocketAddress().getAddress().getHostAddress());
        System.out.println("存储服务器端口: " + group1.getInetSocketAddress().getPort());
        System.out.println("存储服务器脚标: " + group1.getStorePathIndex());
    }

    @Test
    public void getFileGroupInfos() {
        String group_name = "group1";
        String remote_filename = "M00/00/00/wKjThF5OTHiAMQ04AADvuF4tGRU251.jpg";
        ServerInfo[] serverInfos = FastDFSClient.getServerInfo(group_name, remote_filename);
        ServerInfo serverInfo = serverInfos[0];
        System.out.println("存储服务器地址: " + serverInfo.getIpAddr());
        System.out.println("存储服务器端口: " + serverInfo.getPort());
    }

}
