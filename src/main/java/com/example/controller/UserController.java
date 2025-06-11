package com.example.controller;

import com.example.ExceptionHandler.AuthException;
import com.example.dto.LoginDTO;
import com.example.dto.PasswordChangeDTO;
import com.example.interceptor.AuthInterceptor;
import com.example.service.UserService;
import com.example.vo.LoginResponseVO;
import com.example.vo.UserSessionVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        try {
            LoginResponseVO responseVO = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
            HttpSession session = request.getSession(true);
            UserSessionVO userSessionVO = new UserSessionVO();
            userSessionVO.setUserId(responseVO.getUserId());
            userSessionVO.setUsername(responseVO.getUsername());
            userSessionVO.setRole(responseVO.getRole());
            session.setAttribute(AuthInterceptor.USER_SESSION_KEY, userSessionVO);
            return ResponseEntity.ok(responseVO);
        } catch (AuthException e) {
            return ResponseEntity.status(e.getStatus()).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/user/role")
    public ResponseEntity<Map<String, String>> getUserRole(HttpSession session) {
        UserSessionVO user = (UserSessionVO) session.getAttribute(AuthInterceptor.USER_SESSION_KEY);
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
            UserSessionVO user = (UserSessionVO) session.getAttribute(AuthInterceptor.USER_SESSION_KEY);
            userService.changePassword(user.getUserId(), passwordChangeDTO.getCurrentPassword(), passwordChangeDTO.getNewPassword());
            return ResponseEntity.ok(Map.of("message", "密码修改成功"));
        } catch (AuthException e) {
            return ResponseEntity.status(e.getStatus()).body(Map.of("error", e.getMessage()));
        }
    }
}