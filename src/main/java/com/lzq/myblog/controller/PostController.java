package com.lzq.myblog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lzq.myblog.common.Result;
import com.lzq.myblog.dto.PostCreateDTO;
import com.lzq.myblog.dto.PostUpdateDTO;
import com.lzq.myblog.entity.BlogPost;
import com.lzq.myblog.service.BlogPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 博文控制器
 */
@Tag(name = "博文管理", description = "博文的查询、创建、修改、删除操作")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    
    private final BlogPostService blogPostService;
    
    /**
     * 获取博文列表（分页）
     */
    @Operation(summary = "获取博文列表", description = "分页获取已发布的博文列表")
    @GetMapping
    public Result<IPage<BlogPost>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        IPage<BlogPost> result = blogPostService.list(page, size);
        return Result.success(result);
    }
    
    /**
     * 获取博文详情
     */
    @Operation(summary = "获取博文详情", description = "根据 ID 获取博文详情，同时增加浏览量")
    @GetMapping("/{id}")
    public Result<BlogPost> getDetail(@Parameter(description = "博文ID") @PathVariable Long id) {
        BlogPost post = blogPostService.getDetail(id);
        // 增加浏览量
        blogPostService.incrementViewCount(id);
        return Result.success(post);
    }
    
    /**
     * 按分类查询博文
     */
    @Operation(summary = "按分类查询", description = "根据分类 (CTF/LEARN/SOMETHING) 获取博文列表")
    @GetMapping("/category/{category}")
    public Result<IPage<BlogPost>> listByCategory(
            @Parameter(description = "分类名称") @PathVariable String category,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        IPage<BlogPost> result = blogPostService.listByCategory(category, page, size);
        return Result.success(result);
    }
    
    /**
     * 搜索博文
     */
    @Operation(summary = "搜索博文", description = "根据关键词搜索博文标题和内容")
    @GetMapping("/search")
    public Result<IPage<BlogPost>> search(
            @Parameter(description = "搜索关键词") @RequestParam String keyword,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        IPage<BlogPost> result = blogPostService.search(keyword, page, size);
        return Result.success(result);
    }
    
    /**
     * 创建博文（仅管理员）
     */
    @Operation(summary = "创建博文", description = "创建新博文，需要 ADMIN 角色")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<BlogPost> create(@Valid @RequestBody PostCreateDTO createDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long authorId = (Long) authentication.getPrincipal();
        
        BlogPost post = blogPostService.create(createDTO, authorId);
        return Result.success("博文创建成功", post);
    }
    
    /**
     * 更新博文（仅管理员）
     */
    @Operation(summary = "更新博文", description = "更新博文信息，需要 ADMIN 角色")
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<BlogPost> update(@Valid @RequestBody PostUpdateDTO updateDTO) {
        BlogPost post = blogPostService.update(updateDTO);
        return Result.success("博文更新成功", post);
    }
    
    /**
     * 删除博文（仅管理员）
     */
    @Operation(summary = "删除博文", description = "删除指定博文，需要 ADMIN 角色")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> delete(@Parameter(description = "博文ID") @PathVariable Long id) {
        blogPostService.delete(id);
        return Result.success("博文删除成功", null);
    }
}
