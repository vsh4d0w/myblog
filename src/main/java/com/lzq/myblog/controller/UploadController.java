package com.lzq.myblog.controller;

import com.lzq.myblog.common.Result;
import com.lzq.myblog.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "文件上传", description = "图片上传相关接口")
@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {
    
    private final FileService fileService;
    
    @Operation(summary = "上传图片")
    @PostMapping("/image")
    public Result<Map<String, String>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "type", defaultValue = "images") String type) {
        String url = fileService.uploadImage(file, type);
        Map<String, String> result = new HashMap<>();
        result.put("url", url);
        return Result.success(result);
    }
    
    @Operation(summary = "上传头像")
    @PostMapping("/avatar")
    public Result<Map<String, String>> uploadAvatar(@RequestParam("file") MultipartFile file) {
        String url = fileService.uploadImage(file, "avatar");
        Map<String, String> result = new HashMap<>();
        result.put("url", url);
        return Result.success(result);
    }
    
    @Operation(summary = "上传封面图")
    @PostMapping("/cover")
    public Result<Map<String, String>> uploadCover(@RequestParam("file") MultipartFile file) {
        String url = fileService.uploadImage(file, "cover");
        Map<String, String> result = new HashMap<>();
        result.put("url", url);
        return Result.success(result);
    }
}
