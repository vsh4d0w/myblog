package com.lzq.myblog.service;

import java.time.Duration;
import java.util.Set;

/**
 * Redis 缓存服务接口
 */
public interface CacheService {
    
    /**
     * 设置缓存
     */
    void set(String key, Object value);
    
    /**
     * 设置带过期时间的缓存
     */
    void set(String key, Object value, Duration timeout);
    
    /**
     * 获取缓存
     */
    <T> T get(String key, Class<T> clazz);
    
    /**
     * 删除缓存
     */
    void delete(String key);
    
    /**
     * 批量删除缓存
     */
    void deleteByPattern(String pattern);
    
    /**
     * 判断缓存是否存在
     */
    boolean hasKey(String key);
    
    /**
     * 设置过期时间
     */
    boolean expire(String key, Duration timeout);
    
    /**
     * 获取过期时间
     */
    Long getExpire(String key);
    
    /**
     * 自增
     */
    Long increment(String key);
    
    /**
     * 自增指定值
     */
    Long increment(String key, long delta);
    
    /**
     * 获取匹配的所有key
     */
    Set<String> keys(String pattern);
}
