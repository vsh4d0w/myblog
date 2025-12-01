package com.lzq.myblog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 邮件验证码服务
 * 使用内存缓存存储验证码，后续可以切换为 Redis
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    
    private final JavaMailSender mailSender;
    
    @Value("${spring.mail.username:}")
    private String fromEmail;
    
    @Value("${email.code.expire-minutes:5}")
    private int expireMinutes;
    
    @Value("${email.code.length:6}")
    private int codeLength;
    
    /**
     * 验证码缓存：email -> VerificationCode
     */
    private final Map<String, VerificationCode> codeCache = new ConcurrentHashMap<>();
    
    /**
     * 验证码记录
     */
    private record VerificationCode(String code, LocalDateTime expireTime) {
        boolean isExpired() {
            return LocalDateTime.now().isAfter(expireTime);
        }
    }
    
    /**
     * 发送验证码邮件
     * @param toEmail 接收邮箱
     * @return 是否发送成功
     */
    public boolean sendVerificationCode(String toEmail) {
        // 检查是否配置了邮箱
        if (fromEmail == null || fromEmail.isEmpty() || fromEmail.contains("your_email")) {
            log.warn("邮件服务未配置，跳过发送验证码到: {}", toEmail);
            // 开发模式：生成验证码但不发送，返回成功让开发继续
            String code = generateCode();
            codeCache.put(toEmail, new VerificationCode(code, LocalDateTime.now().plusMinutes(expireMinutes)));
            log.info("【开发模式】验证码: {} (邮箱: {})", code, toEmail);
            return true;
        }
        
        try {
            // 生成验证码
            String code = generateCode();
            
            // 存储验证码
            codeCache.put(toEmail, new VerificationCode(code, LocalDateTime.now().plusMinutes(expireMinutes)));
            
            // 发送邮件
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("【myBlog】邮箱验证码");
            message.setText(String.format(
                    "您好！\n\n" +
                    "您正在注册 myBlog 博客系统账号，验证码为：\n\n" +
                    "    %s\n\n" +
                    "验证码有效期为 %d 分钟，请尽快完成注册。\n\n" +
                    "如果这不是您本人的操作，请忽略此邮件。\n\n" +
                    "—— myBlog 团队",
                    code, expireMinutes
            ));
            
            mailSender.send(message);
            log.info("验证码邮件发送成功: {}", toEmail);
            return true;
            
        } catch (Exception e) {
            log.error("发送验证码邮件失败: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 验证验证码
     * @param email 邮箱
     * @param code 验证码
     * @return 是否验证成功
     */
    public boolean verifyCode(String email, String code) {
        VerificationCode stored = codeCache.get(email);
        
        if (stored == null) {
            log.debug("验证码不存在: {}", email);
            return false;
        }
        
        if (stored.isExpired()) {
            codeCache.remove(email);
            log.debug("验证码已过期: {}", email);
            return false;
        }
        
        if (!stored.code().equals(code)) {
            log.debug("验证码错误: {}", email);
            return false;
        }
        
        // 验证成功，移除验证码
        codeCache.remove(email);
        return true;
    }
    
    /**
     * 生成随机验证码
     */
    private String generateCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
    
    /**
     * 清理过期验证码（可定时调用）
     */
    public void cleanExpiredCodes() {
        codeCache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
}
