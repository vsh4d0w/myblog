package com.lzq.myblog.service.impl;

import com.lzq.myblog.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis 缓存服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    
    private static final String KEY_PREFIX = "myblog:";
    
    @Override
    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(KEY_PREFIX + key, value);
        } catch (Exception e) {
            log.error("Redis set error: key={}", key, e);
        }
    }
    
    @Override
    public void set(String key, Object value, Duration timeout) {
        try {
            redisTemplate.opsForValue().set(KEY_PREFIX + key, value, timeout);
        } catch (Exception e) {
            log.error("Redis set with timeout error: key={}", key, e);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        try {
            Object value = redisTemplate.opsForValue().get(KEY_PREFIX + key);
            if (value != null && clazz.isInstance(value)) {
                return (T) value;
            }
            return null;
        } catch (Exception e) {
            log.error("Redis get error: key={}", key, e);
            return null;
        }
    }
    
    @Override
    public void delete(String key) {
        try {
            redisTemplate.delete(KEY_PREFIX + key);
        } catch (Exception e) {
            log.error("Redis delete error: key={}", key, e);
        }
    }
    
    @Override
    public void deleteByPattern(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(KEY_PREFIX + pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.info("Deleted {} keys matching pattern: {}", keys.size(), pattern);
            }
        } catch (Exception e) {
            log.error("Redis deleteByPattern error: pattern={}", pattern, e);
        }
    }
    
    @Override
    public boolean hasKey(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(KEY_PREFIX + key));
        } catch (Exception e) {
            log.error("Redis hasKey error: key={}", key, e);
            return false;
        }
    }
    
    @Override
    public boolean expire(String key, Duration timeout) {
        try {
            return Boolean.TRUE.equals(redisTemplate.expire(KEY_PREFIX + key, timeout));
        } catch (Exception e) {
            log.error("Redis expire error: key={}", key, e);
            return false;
        }
    }
    
    @Override
    public Long getExpire(String key) {
        try {
            return redisTemplate.getExpire(KEY_PREFIX + key, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Redis getExpire error: key={}", key, e);
            return null;
        }
    }
    
    @Override
    public Long increment(String key) {
        return increment(key, 1);
    }
    
    @Override
    public Long increment(String key, long delta) {
        try {
            return redisTemplate.opsForValue().increment(KEY_PREFIX + key, delta);
        } catch (Exception e) {
            log.error("Redis increment error: key={}", key, e);
            return null;
        }
    }
    
    @Override
    public Set<String> keys(String pattern) {
        try {
            return redisTemplate.keys(KEY_PREFIX + pattern);
        } catch (Exception e) {
            log.error("Redis keys error: pattern={}", pattern, e);
            return Set.of();
        }
    }
}
