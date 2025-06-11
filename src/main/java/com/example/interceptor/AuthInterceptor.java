package com.example.interceptor;

import com.example.ExceptionHandler.AuthException;
import com.example.vo.UserSessionVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    public static final String USER_SESSION_KEY = "user";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(USER_SESSION_KEY) == null) {
            throw new AuthException(HttpStatus.UNAUTHORIZED, "用户未登录或会话已过期");
        }

        UserSessionVO user = (UserSessionVO) session.getAttribute(USER_SESSION_KEY);
        String path = request.getRequestURI();

        if (path.startsWith("/api/admin")) {
            if (!"admin".equals(user.getRole())) {
                throw new AuthException(HttpStatus.FORBIDDEN, "无权访问此资源");
            }
        }

        return true;
    }
}