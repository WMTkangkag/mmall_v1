package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by txk on 2018/6/5.
 */
public interface IFileService {

    String upload(MultipartFile file, String path);

}
