package com.lzq.myblog.dto;

import com.lzq.myblog.entity.BlogPost;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 博文详情响应 DTO
 * 包含 Markdown 原文和渲染后的 HTML
 */
@Data
public class PostDetailDTO {
    
    /** 博文ID */
    private Long id;
    
    /** 博文标题 */
    private String title;
    
    /** Markdown 原文内容 */
    private String content;
    
    /** 渲染后的 HTML 内容 */
    private String contentHtml;
    
    /** 博文摘要 */
    private String summary;
    
    /** 分类 */
    private String category;
    
    /** 封面图片URL */
    private String coverImage;
    
    /** 作者ID */
    private Long authorId;
    
    /** 作者用户名 */
    private String authorName;
    
    /** 浏览量 */
    private Integer viewCount;
    
    /** 状态：1-发布，0-草稿 */
    private Integer status;
    
    /** 创建时间 */
    private LocalDateTime createTime;
    
    /** 更新时间 */
    private LocalDateTime updateTime;
    
    /**
     * 从 BlogPost 实体转换
     */
    public static PostDetailDTO fromEntity(BlogPost post, String contentHtml) {
        PostDetailDTO dto = new PostDetailDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setContentHtml(contentHtml);
        dto.setSummary(post.getSummary());
        dto.setCategory(post.getCategory());
        dto.setCoverImage(post.getCoverImage());
        dto.setAuthorId(post.getAuthorId());
        dto.setViewCount(post.getViewCount());
        dto.setStatus(post.getStatus());
        dto.setCreateTime(post.getCreateTime());
        dto.setUpdateTime(post.getUpdateTime());
        return dto;
    }
}
