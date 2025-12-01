package com.lzq.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzq.myblog.entity.PostTag;
import com.lzq.myblog.entity.Tag;
import com.lzq.myblog.mapper.PostTagMapper;
import com.lzq.myblog.mapper.TagMapper;
import com.lzq.myblog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    
    private final TagMapper tagMapper;
    private final PostTagMapper postTagMapper;
    
    // 预设的标签颜色
    private static final String[] COLORS = {
        "#409eff", "#67c23a", "#e6a23c", "#f56c6c", "#909399",
        "#00d4ff", "#ff6b6b", "#4ecdc4", "#45b7d1", "#96ceb4"
    };
    
    @Override
    public List<Tag> getAllTags() {
        return list(new LambdaQueryWrapper<Tag>().orderByDesc(Tag::getUseCount));
    }
    
    @Override
    public List<Tag> getHotTags(Integer limit) {
        return tagMapper.selectHotTags(limit != null ? limit : 10);
    }
    
    @Override
    public List<Tag> getTagsByPostId(Long postId) {
        return tagMapper.selectTagsByPostId(postId);
    }
    
    @Override
    @Transactional
    public Tag getOrCreateTag(String name) {
        Tag tag = getOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName, name));
        if (tag == null) {
            tag = new Tag();
            tag.setName(name);
            tag.setColor(COLORS[new Random().nextInt(COLORS.length)]);
            tag.setUseCount(0);
            save(tag);
        }
        return tag;
    }
    
    @Override
    @Transactional
    public void setPostTags(Long postId, List<Long> tagIds) {
        // 删除原有关联
        postTagMapper.deleteByPostId(postId);
        
        // 添加新关联
        if (tagIds != null && !tagIds.isEmpty()) {
            for (Long tagId : tagIds) {
                PostTag postTag = new PostTag();
                postTag.setPostId(postId);
                postTag.setTagId(tagId);
                postTagMapper.insert(postTag);
                
                // 增加使用次数
                Tag tag = getById(tagId);
                if (tag != null) {
                    tag.setUseCount(tag.getUseCount() + 1);
                    updateById(tag);
                }
            }
        }
    }
    
    @Override
    @Transactional
    public void setPostTagsByName(Long postId, List<String> tagNames) {
        // 删除原有关联
        postTagMapper.deleteByPostId(postId);
        
        // 添加新关联
        if (tagNames != null && !tagNames.isEmpty()) {
            List<Long> tagIds = new ArrayList<>();
            for (String name : tagNames) {
                Tag tag = getOrCreateTag(name.trim());
                tagIds.add(tag.getId());
            }
            
            for (Long tagId : tagIds) {
                PostTag postTag = new PostTag();
                postTag.setPostId(postId);
                postTag.setTagId(tagId);
                postTagMapper.insert(postTag);
                
                // 增加使用次数
                Tag tag = getById(tagId);
                if (tag != null) {
                    tag.setUseCount(tag.getUseCount() + 1);
                    updateById(tag);
                }
            }
        }
    }
}
