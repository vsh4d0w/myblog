package com.lzq.myblog.service.impl;

import com.lzq.myblog.exception.BusinessException;
import com.lzq.myblog.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    
    @Value("${file.upload.path:uploads}")
    private String uploadPath;
    
    @Value("${file.upload.max-size:5242880}")
    private long maxSize; // 默认 5MB
    
    private static final List<String> ALLOWED_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );
    
    @Override
    public String uploadImage(MultipartFile file, String type) {
        // 检查文件
        if (file == null || file.isEmpty()) {
            throw new BusinessException("请选择要上传的文件");
        }
        
        // 检查文件大小
        if (file.getSize() > maxSize) {
            throw new BusinessException("文件大小不能超过 " + (maxSize / 1024 / 1024) + "MB");
        }
        
        // 检查文件类型
        String contentType = file.getContentType();
        if (!ALLOWED_TYPES.contains(contentType)) {
            throw new BusinessException("只支持 JPG, PNG, GIF, WEBP 格式的图片");
        }
        
        // 生成文件名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".") 
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : ".jpg";
        String filename = UUID.randomUUID().toString().replace("-", "") + extension;
        
        // 创建目录
        String subDir = type != null ? type : "images";
        Path directory = Paths.get(uploadPath, subDir);
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new BusinessException("创建上传目录失败");
        }
        
        // 保存文件
        Path filePath = directory.resolve(filename);
        try {
            Files.copy(file.getInputStream(), filePath);
        } catch (IOException e) {
            throw new BusinessException("文件保存失败");
        }
        
        // 返回访问URL
        return "/uploads/" + subDir + "/" + filename;
    }
    
    @Override
    public void deleteFile(String url) {
        if (url == null || !url.startsWith("/uploads/")) {
            return;
        }
        
        String relativePath = url.substring("/uploads/".length());
        Path filePath = Paths.get(uploadPath, relativePath);
        
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // 忽略删除失败
        }
    }
}
