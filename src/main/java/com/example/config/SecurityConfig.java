package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Resource
    private ObjectMapper objectMapper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/login", "/api/register").permitAll()
                            .requestMatchers("/api/admin/**").hasAuthority("admin")
                            .anyRequest().authenticated()
                    )
                .formLogin(form -> form
                        .successHandler((request, response, authentication) -> {
                        })
                        .failureHandler((request, response, exception) -> {
                            // 实际的登录失败逻辑在UserController中处理，这里配置返回401
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                            Map<String, String> error = Map.of("error", "用户名或密码无效");
                            response.getWriter().write(objectMapper.writeValueAsString(error));
                        })
                )
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                            Map<String, String> success = Map.of("message", "登出成功");
                            response.getWriter().write(objectMapper.writeValueAsString(success));
                        })
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                            Map<String, String> error = Map.of("error", "用户未认证，请先登录");
                            response.getWriter().write(objectMapper.writeValueAsString(error));
                        })
                        // 为403 Forbidden也提供JSON响应
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                             response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                             response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                             response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                             Map<String, String> error = Map.of("error", "权限不足");
                             response.getWriter().write(objectMapper.writeValueAsString(error));
                        })
                );

        return http.build();
    }
}