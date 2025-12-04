package com.lzq.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzq.myblog.entity.PostLike;
import com.lzq.myblog.mapper.PostLikeMapper;
import com.lzq.myblog.service.InteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InteractionServiceImpl implements InteractionService {
    
    private final PostLikeMapper postLikeMapper;
    
    @Override
    public boolean likePost(Long postId, Long userId) {
        if (isLiked(postId, userId)) {
            return false;
        }
        PostLike like = new PostLike();
        like.setPostId(postId);
        like.setUserId(userId);
        return postLikeMapper.insert(like) > 0;
    }
    
    @Override
    public boolean unlikePost(Long postId, Long userId) {
        return postLikeMapper.delete(new LambdaQueryWrapper<PostLike>()
                .eq(PostLike::getPostId, postId)
                .eq(PostLike::getUserId, userId)) > 0;
    }
    
    @Override
    public boolean isLiked(Long postId, Long userId) {
        return postLikeMapper.checkLiked(postId, userId) > 0;
    }
    
    @Override
    public int getLikeCount(Long postId) {
        return postLikeMapper.countByPostId(postId);
    }
}
