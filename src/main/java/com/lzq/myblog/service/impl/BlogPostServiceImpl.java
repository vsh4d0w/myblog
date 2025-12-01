package com.lzq.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzq.myblog.dto.PostCreateDTO;
import com.lzq.myblog.dto.PostUpdateDTO;
import com.lzq.myblog.entity.BlogPost;
import com.lzq.myblog.exception.BusinessException;
import com.lzq.myblog.mapper.BlogPostMapper;
import com.lzq.myblog.service.BlogPostService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 博文服务实现
 */
@Service
public class BlogPostServiceImpl extends ServiceImpl<BlogPostMapper, BlogPost> implements BlogPostService {
    
    @Override
    public BlogPost create(PostCreateDTO createDTO, Long authorId) {
        BlogPost post = new BlogPost();
        post.setTitle(createDTO.getTitle());
        post.setContent(createDTO.getContent());
        
        // 摘要：如果没有提供，自动截取内容前200字
        if (StringUtils.hasText(createDTO.getSummary())) {
            post.setSummary(createDTO.getSummary());
        } else {
            String content = createDTO.getContent();
            post.setSummary(content.length() > 200 ? content.substring(0, 200) + "..." : content);
        }
        
        post.setCategory(createDTO.getCategory().toUpperCase());
        post.setCoverImage(createDTO.getCoverImage());
        post.setAuthorId(authorId);
        post.setViewCount(0);
        post.setStatus(createDTO.getStatus() != null ? createDTO.getStatus() : 1);
        
        baseMapper.insert(post);
        return post;
    }
    
    @Override
    public BlogPost update(PostUpdateDTO updateDTO) {
        BlogPost post = baseMapper.selectById(updateDTO.getId());
        if (post == null) {
            throw new BusinessException("博文不存在");
        }
        
        if (StringUtils.hasText(updateDTO.getTitle())) {
            post.setTitle(updateDTO.getTitle());
        }
        if (StringUtils.hasText(updateDTO.getContent())) {
            post.setContent(updateDTO.getContent());
        }
        if (StringUtils.hasText(updateDTO.getSummary())) {
            post.setSummary(updateDTO.getSummary());
        }
        if (StringUtils.hasText(updateDTO.getCategory())) {
            post.setCategory(updateDTO.getCategory().toUpperCase());
        }
        if (updateDTO.getCoverImage() != null) {
            post.setCoverImage(updateDTO.getCoverImage());
        }
        if (updateDTO.getStatus() != null) {
            post.setStatus(updateDTO.getStatus());
        }
        
        baseMapper.updateById(post);
        return post;
    }
    
    @Override
    public boolean delete(Long postId) {
        BlogPost post = baseMapper.selectById(postId);
        if (post == null) {
            throw new BusinessException("博文不存在");
        }
        return baseMapper.deleteById(postId) > 0;
    }
    
    @Override
    public BlogPost getDetail(Long postId) {
        BlogPost post = baseMapper.selectById(postId);
        if (post == null) {
            throw new BusinessException("博文不存在");
        }
        return post;
    }
    
    @Override
    public IPage<BlogPost> list(int page, int size) {
        Page<BlogPost> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<BlogPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlogPost::getStatus, 1)  // 只查询已发布的
               .orderByDesc(BlogPost::getCreateTime);
        return baseMapper.selectPage(pageParam, wrapper);
    }
    
    @Override
    public IPage<BlogPost> listByCategory(String category, int page, int size) {
        Page<BlogPost> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<BlogPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlogPost::getCategory, category.toUpperCase())
               .eq(BlogPost::getStatus, 1)
               .orderByDesc(BlogPost::getCreateTime);
        return baseMapper.selectPage(pageParam, wrapper);
    }
    
    @Override
    public IPage<BlogPost> search(String keyword, int page, int size) {
        Page<BlogPost> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<BlogPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.like(BlogPost::getTitle, keyword)
                         .or()
                         .like(BlogPost::getContent, keyword))
               .eq(BlogPost::getStatus, 1)
               .orderByDesc(BlogPost::getCreateTime);
        return baseMapper.selectPage(pageParam, wrapper);
    }
    
    @Override
    public void incrementViewCount(Long postId) {
        baseMapper.incrementViewCount(postId);
    }
}
