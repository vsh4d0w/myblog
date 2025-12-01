package com.lzq.myblog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lzq.myblog.common.Result;
import com.lzq.myblog.dto.PostCreateDTO;
import com.lzq.myblog.dto.PostUpdateDTO;
import com.lzq.myblog.entity.BlogPost;
import com.lzq.myblog.service.BlogPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 博文控制器
 */
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    
    private final BlogPostService blogPostService;
    
    /**
     * 获取博文列表（分页）
     */
    @GetMapping
    public Result<IPage<BlogPost>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPage<BlogPost> result = blogPostService.list(page, size);
        return Result.success(result);
    }
    
    /**
     * 获取博文详情
     */
    @GetMapping("/{id}")
    public Result<BlogPost> getDetail(@PathVariable Long id) {
        BlogPost post = blogPostService.getDetail(id);
        // 增加浏览量
        blogPostService.incrementViewCount(id);
        return Result.success(post);
    }
    
    /**
     * 按分类查询博文
     */
    @GetMapping("/category/{category}")
    public Result<IPage<BlogPost>> listByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPage<BlogPost> result = blogPostService.listByCategory(category, page, size);
        return Result.success(result);
    }
    
    /**
     * 搜索博文
     */
    @GetMapping("/search")
    public Result<IPage<BlogPost>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPage<BlogPost> result = blogPostService.search(keyword, page, size);
        return Result.success(result);
    }
    
    /**
     * 创建博文（仅管理员）
     */
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
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<BlogPost> update(@Valid @RequestBody PostUpdateDTO updateDTO) {
        BlogPost post = blogPostService.update(updateDTO);
        return Result.success("博文更新成功", post);
    }
    
    /**
     * 删除博文（仅管理员）
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> delete(@PathVariable Long id) {
        blogPostService.delete(id);
        return Result.success("博文删除成功", null);
    }
}
