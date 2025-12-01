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
    
    /**
     * 从URL下载图片并保存到本地
     * @param imageUrl 外部图片URL
     * @param type 类型 (posts, avatar 等)
     * @return 本地文件访问URL，如果下载失败返回原URL
     */
    String downloadAndSaveImage(String imageUrl, String type);
    
    /**
     * 处理Markdown内容中的外部图片，下载并替换为本地路径
     * @param content Markdown内容
     * @param type 保存类型
     * @return 处理后的内容
     */
    String processMarkdownImages(String content, String type);
}
