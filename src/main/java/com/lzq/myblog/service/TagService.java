package com.lzq.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzq.myblog.entity.Tag;

import java.util.List;

public interface TagService extends IService<Tag> {
    
    /**
     * 获取所有标签
     */
    List<Tag> getAllTags();
    
    /**
     * 获取热门标签
     */
    List<Tag> getHotTags(Integer limit);
    
    /**
     * 获取博文的标签
     */
    List<Tag> getTagsByPostId(Long postId);
    
    /**
     * 创建或获取标签
     */
    Tag getOrCreateTag(String name);
    
    /**
     * 为博文设置标签
     */
    void setPostTags(Long postId, List<Long> tagIds);
    
    /**
     * 为博文设置标签（通过名称）
     */
    void setPostTagsByName(Long postId, List<String> tagNames);
}
