package com.lzq.myblog.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    
    /**
     * 上传图片
     * @param file 文件
     * @param type 类型 (avatar, cover, content)
     * @return 文件访问URL
     */
    String uploadImage(MultipartFile file, String type);
    
    /**
     * 删除文件
     * @param url 文件URL
     */
    void deleteFile(String url);
}
