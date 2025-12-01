package com.lzq.myblog.controller;

import com.lzq.myblog.common.Result;
import com.lzq.myblog.entity.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分类控制器
 */
@Tag(name = "分类管理", description = "博文分类的查询操作")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    /**
     * 获取所有分类
     */
    @Operation(summary = "获取所有分类", description = "获取博客系统支持的所有分类")
    @GetMapping
    public Result<List<Map<String, Object>>> getAllCategories() {
        List<Map<String, Object>> categories = Arrays.stream(Category.values())
                .map(category -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", category.ordinal() + 1);
                    map.put("name", category.name());
                    map.put("displayName", category.getValue());
                    return map;
                })
                .collect(Collectors.toList());
        return Result.success(categories);
    }

    /**
     * 获取分类详情
     */
    @Operation(summary = "获取分类详情", description = "根据ID获取分类详情")
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getCategoryById(@Parameter(description = "分类ID") @PathVariable Integer id) {
        Category[] categories = Category.values();
        if (id < 1 || id > categories.length) {
            return Result.error("分类不存在");
        }
        
        Category category = categories[id - 1];
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", category.name());
        map.put("displayName", category.getValue());
        return Result.success(map);
    }
}
