package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by txk on 2018/6/5.
 */
@Service("iFileService")
@Slf4j
public class FileServiceImpl implements IFileService {


    public String upload(MultipartFile file,String path){
        String fileName=file.getOriginalFilename();
        //获取扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName= UUID.randomUUID().toString()+"."+fileExtensionName;
        log.info("开始上传文件，上传文件的文件名:{}，上传路径:{}，新文件名:{}",fileName,path,uploadFileName);
        File fileDir=new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile=new File(path,uploadFileName);
        try {
            //上传文件到tomcat指定的upload目录下
            file.transferTo(targetFile);
            //再把tomcat的upload中的文件上传到ftp服务器
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //上传完成之后，删除upload下面的文件
            targetFile.delete();

        } catch (IOException e) {
            log.info("上传文件异常",e);
            return null;
        }
        return targetFile.getName();
    }
}
