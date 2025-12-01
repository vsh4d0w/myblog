package com.lzq.myblog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 创建博文请求DTO
 */
@Data
public class PostCreateDTO {
    
    @NotBlank(message = "标题不能为空")
    private String title;
    
    @NotBlank(message = "内容不能为空")
    private String content;
    
    /** 摘要（可选，不填则自动截取内容前200字） */
    private String summary;
    
    /** 分类：CTF / Learn / Something */
    @NotBlank(message = "分类不能为空")
    private String category;
    
    /** 封面图片URL（可选） */
    private String coverImage;
    
    /** 状态：1-发布，0-草稿，默认发布 */
    private Integer status = 1;
}
