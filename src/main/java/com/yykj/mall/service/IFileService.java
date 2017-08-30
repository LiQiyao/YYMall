package com.yykj.mall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Lee on 2017/8/18.
 */
public interface IFileService {

    String upload(MultipartFile file, String path);
}
