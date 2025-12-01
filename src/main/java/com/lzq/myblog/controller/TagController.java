package com.lzq.myblog.controller;

import com.lzq.myblog.common.Result;
import com.lzq.myblog.entity.Tag;
import com.lzq.myblog.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@io.swagger.v3.oas.annotations.tags.Tag(name = "标签管理", description = "标签相关接口")
@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    
    private final TagService tagService;
    
    @Operation(summary = "获取所有标签")
    @GetMapping
    public Result<List<Tag>> getAllTags() {
        return Result.success(tagService.getAllTags());
    }
    
    @Operation(summary = "获取热门标签")
    @GetMapping("/hot")
    public Result<List<Tag>> getHotTags(@RequestParam(defaultValue = "10") Integer limit) {
        return Result.success(tagService.getHotTags(limit));
    }
    
    @Operation(summary = "获取博文的标签")
    @GetMapping("/post/{postId}")
    public Result<List<Tag>> getPostTags(@PathVariable Long postId) {
        return Result.success(tagService.getTagsByPostId(postId));
    }
    
    @Operation(summary = "创建标签")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Tag> createTag(@RequestBody Tag tag) {
        tagService.save(tag);
        return Result.success(tag);
    }
    
    @Operation(summary = "删除标签")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteTag(@PathVariable Long id) {
        tagService.removeById(id);
        return Result.success();
    }
}
