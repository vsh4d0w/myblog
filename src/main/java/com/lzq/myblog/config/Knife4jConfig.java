package com.lzq.myblog.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j (OpenAPI 3) 配置
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("个人博客系统 API 文档")
                        .version("1.0.0")
                        .description("myBlog 个人博客系统后端接口文档\n\n" +
                                "## 认证说明\n" +
                                "- 公开接口无需认证\n" +
                                "- 需要认证的接口请在请求头添加: `Authorization: Bearer <token>`\n" +
                                "- 管理员接口需要 ADMIN 角色\n\n" +
                                "## 管理员账号\n" +
                                "- 用户名: sh4d0w\n" +
                                "- 密码: 467194403")
                        .contact(new Contact()
                                .name("sh4d0w")
                                .email("sh4d0w@example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                // 添加 JWT 认证配置
                .addSecurityItem(new SecurityRequirement().addList("JWT"))
                .components(new Components()
                        .addSecuritySchemes("JWT", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                                .description("JWT Token，格式: Bearer <token>")));
    }
}
