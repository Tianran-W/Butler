package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. 禁用 CSRF 防护，便于测试
                .csrf(AbstractHttpConfigurer::disable)

                // 2. 配置请求授权规则
                .authorizeHttpRequests(auth -> auth
                        // 允许登录接口，任何人都可以访问
                        .requestMatchers("/api/login").permitAll()

                        // 所有以 /api/admin/ 开头的接口，只有角色为 'admin' 的用户才能访问
                        // 注意：你的 UserSessionVO 中 role 字段存的是 "admin"，这里会自动匹配
                        .requestMatchers("/api/admin/**").hasAuthority("admin")

                        // 其他所有 /api/ 开头的接口，只要登录了就能访问
                        .requestMatchers("/api/**").authenticated()

                        // （可选）除了以上规则，其他任何请求都拒绝访问，增强安全性
                        .anyRequest().denyAll()
                );

        return http.build();
    }
}