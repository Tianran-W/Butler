package com.example.controller;

import com.example.ExceptionHandler.AuthException;
import com.example.dto.LoginDTO;
import com.example.dto.PasswordChangeDTO;
import com.example.service.UserService;
import com.example.vo.LoginResponseVO;
import com.example.vo.UserSessionVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Resource
    private UserService userService;

    private static final String USER_INFO_SESSION_KEY = "user_info";

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        try {
            LoginResponseVO responseVO = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
            HttpSession session = request.getSession(true);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    responseVO.getUsername(),
                    null,
                    List.of(new SimpleGrantedAuthority(responseVO.getRole()))
            );
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authToken);
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

            UserSessionVO userSessionVO = new UserSessionVO();
            userSessionVO.setUserId(responseVO.getUserId());
            userSessionVO.setUsername(responseVO.getUsername());
            userSessionVO.setRole(responseVO.getRole());

            session.setAttribute(USER_INFO_SESSION_KEY, userSessionVO);

            return ResponseEntity.ok(responseVO);
        } catch (AuthException e) {
            return ResponseEntity.status(e.getStatus()).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/user/role")
    public ResponseEntity<Map<String, String>> getUserRole(HttpSession session) {
        UserSessionVO user = (UserSessionVO) session.getAttribute(USER_INFO_SESSION_KEY);
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("error", "用户未登录或会话已过期"));
        }
        return ResponseEntity.ok(Map.of("role", user.getRole()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "登出成功"));
    }

    @PutMapping("/user/password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO, HttpSession session) {
        try {
            UserSessionVO user = (UserSessionVO) session.getAttribute(USER_INFO_SESSION_KEY);
            if (user == null) {
                throw new AuthException(org.springframework.http.HttpStatus.UNAUTHORIZED, "用户未登录或会话已过期");
            }
            userService.changePassword(user.getUserId(), passwordChangeDTO.getCurrentPassword(), passwordChangeDTO.getNewPassword());
            return ResponseEntity.ok(Map.of("message", "密码修改成功"));
        } catch (AuthException e) {
            return ResponseEntity.status(e.getStatus()).body(Map.of("error", e.getMessage()));
        }
    }
}