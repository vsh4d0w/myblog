package com.lzq.myblog.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 更新博文请求DTO
 */
@Data
public class PostUpdateDTO {
    
    @NotNull(message = "博文ID不能为空")
    private Long id;
    
    /** 标题 */
    private String title;
    
    /** 内容 */
    private String content;
    
    /** 摘要 */
    private String summary;
    
    /** 分类：CTF / Learn / Something */
    private String category;
    
    /** 封面图片URL */
    private String coverImage;
    
    /** 状态：1-发布，0-草稿 */
    private Integer status;
}
