package com.lzq.myblog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 博文实体类
 */
@Data
@TableName("blog_post")
public class BlogPost {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 博文标题 */
    private String title;
    
    /** 博文内容 */
    private String content;
    
    /** 博文摘要 */
    private String summary;
    
    /** 分类：CTF, Learn, Something */
    private String category;
    
    /** 封面图片URL */
    private String coverImage;
    
    /** 作者ID（博主） */
    private Long authorId;
    
    /** 浏览量 */
    private Integer viewCount;
    
    /** 状态：1-发布，0-草稿 */
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

