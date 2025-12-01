package com.lzq.myblog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Markdown 渲染请求 DTO
 */
@Data
public class MarkdownRenderDTO {
    
    /**
     * Markdown 原文内容
     */
    @NotBlank(message = "Markdown内容不能为空")
    private String markdown;
    
    /**
     * 摘要最大长度（用于提取摘要时）
     */
    private Integer maxLength;
}
