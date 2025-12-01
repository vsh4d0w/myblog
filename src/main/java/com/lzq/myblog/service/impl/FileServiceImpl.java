package com.lzq.myblog.service.impl;

import com.lzq.myblog.exception.BusinessException;
import com.lzq.myblog.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FileServiceImpl implements FileService {
    
    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);
    
    @Value("${file.upload.path:uploads}")
    private String uploadPath;
    
    @Value("${file.upload.max-size:5242880}")
    private long maxSize; // 默认 5MB
    
    private static final List<String> ALLOWED_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );
    
    // 匹配 Markdown 图片语法: ![alt](url)
    private static final Pattern MARKDOWN_IMAGE_PATTERN = Pattern.compile(
            "!\\[([^\\]]*)\\]\\(([^)]+)\\)"
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
    
    @Override
    public String downloadAndSaveImage(String imageUrl, String type) {
        // 跳过已经是本地上传路径的图片
        if (imageUrl == null || imageUrl.startsWith("/uploads/") || imageUrl.startsWith("http://localhost")) {
            return imageUrl;
        }
        
        // 处理本地文件路径 (如 /Users/xxx/Pictures/...)
        if (imageUrl.startsWith("/") && !imageUrl.startsWith("/uploads/")) {
            return copyLocalFile(imageUrl, type);
        }
        
        // 只处理 http/https 开头的外部图片
        if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
            return imageUrl;
        }
        
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                log.warn("下载图片失败，HTTP状态码: {}, URL: {}", responseCode, imageUrl);
                return imageUrl;
            }
            
            // 获取文件扩展名
            String contentType = connection.getContentType();
            String extension = getExtensionFromContentType(contentType);
            if (extension == null) {
                extension = getExtensionFromUrl(imageUrl);
            }
            if (extension == null) {
                extension = ".jpg";
            }
            
            // 生成文件名
            String filename = UUID.randomUUID().toString().replace("-", "") + extension;
            
            // 创建目录
            String subDir = type != null ? type : "posts";
            Path directory = Paths.get(uploadPath, subDir);
            Files.createDirectories(directory);
            
            // 下载并保存
            Path filePath = directory.resolve(filename);
            try (InputStream in = connection.getInputStream()) {
                Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
            
            String localUrl = "/uploads/" + subDir + "/" + filename;
            log.info("图片下载成功: {} -> {}", imageUrl, localUrl);
            return localUrl;
            
        } catch (Exception e) {
            log.warn("下载图片失败: {}, 错误: {}", imageUrl, e.getMessage());
            return imageUrl; // 下载失败返回原URL
        }
    }
    
    @Override
    public String processMarkdownImages(String content, String type) {
        if (content == null || content.isEmpty()) {
            return content;
        }
        
        Matcher matcher = MARKDOWN_IMAGE_PATTERN.matcher(content);
        StringBuffer result = new StringBuffer();
        
        while (matcher.find()) {
            String altText = matcher.group(1);
            String imageUrl = matcher.group(2);
            
            // 下载并保存图片
            String localUrl = downloadAndSaveImage(imageUrl, type);
            
            // 如果成功下载，使用本地URL；否则保持原样
            String replacement = "![" + altText + "](" + localUrl + ")";
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);
        
        return result.toString();
    }
    
    private String getExtensionFromContentType(String contentType) {
        if (contentType == null) return null;
        if (contentType.contains("jpeg") || contentType.contains("jpg")) return ".jpg";
        if (contentType.contains("png")) return ".png";
        if (contentType.contains("gif")) return ".gif";
        if (contentType.contains("webp")) return ".webp";
        return null;
    }
    
    private String getExtensionFromUrl(String url) {
        try {
            String path = new URL(url).getPath().toLowerCase();
            if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return ".jpg";
            if (path.endsWith(".png")) return ".png";
            if (path.endsWith(".gif")) return ".gif";
            if (path.endsWith(".webp")) return ".webp";
        } catch (Exception ignored) {}
        return null;
    }
    
    /**
     * 复制本地文件到上传目录
     * @param localPath 本地文件绝对路径
     * @param type 保存类型
     * @return 上传后的访问URL
     */
    private String copyLocalFile(String localPath, String type) {
        try {
            Path sourcePath = Paths.get(localPath);
            
            // 检查文件是否存在
            if (!Files.exists(sourcePath)) {
                log.warn("本地文件不存在: {}", localPath);
                return localPath;
            }
            
            // 检查是否是图片文件
            String fileName = sourcePath.getFileName().toString().toLowerCase();
            if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg") 
                    && !fileName.endsWith(".png") && !fileName.endsWith(".gif") 
                    && !fileName.endsWith(".webp")) {
                log.warn("不支持的图片格式: {}", localPath);
                return localPath;
            }
            
            // 获取扩展名
            String extension = fileName.substring(fileName.lastIndexOf("."));
            
            // 生成新文件名
            String newFilename = UUID.randomUUID().toString().replace("-", "") + extension;
            
            // 创建目录
            String subDir = type != null ? type : "posts";
            Path directory = Paths.get(uploadPath, subDir);
            Files.createDirectories(directory);
            
            // 复制文件
            Path targetPath = directory.resolve(newFilename);
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            
            String localUrl = "/uploads/" + subDir + "/" + newFilename;
            log.info("本地图片复制成功: {} -> {}", localPath, localUrl);
            return localUrl;
            
        } catch (Exception e) {
            log.warn("复制本地图片失败: {}, 错误: {}", localPath, e.getMessage());
            return localPath;
        }
    }
}
