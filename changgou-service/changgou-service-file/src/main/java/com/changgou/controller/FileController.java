package com.changgou.controller;

import com.changgou.file.FastDFSFile;
import com.changgou.util.FastDFSClient;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/20
 */
@RestController
@CrossOrigin  //开启跨域访问
public class FileController {

    @RequestMapping("/update")
    public String upload(@RequestParam("file")MultipartFile file) throws Exception {
        FastDFSFile fastDFSFile = new FastDFSFile(file.getOriginalFilename(),
                                                file.getBytes(), StringUtils.getFilenameExtension(file.getOriginalFilename()));
        String[] uploads = FastDFSClient.uploadFile(fastDFSFile);
        return FastDFSClient.getTrackerUrl()+"/"+uploads[0]+"/"+uploads[1];
    }
}
