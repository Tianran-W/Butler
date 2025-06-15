package com.example.controller;

import com.example.dto.LoginDTO;
import com.example.dto.PasswordChangeDTO;
import com.example.dto.RegisterDTO; 
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.example.vo.LoginResponseVO;
import com.example.entity.User;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid; 
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterDTO registerDTO) {
        userService.register(registerDTO);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", true);
        responseBody.put("message", "注册成功");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 重要：将SecurityContext存入Session
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            User user = userMapper.findByUsername(loginDTO.getUsername());
            LoginResponseVO responseVO = new LoginResponseVO();
            responseVO.setMessage("登录成功");
            responseVO.setUserId(user.getUserId());
            responseVO.setUsername(user.getUsername());
            responseVO.setRole(user.getRoleName());
            return ResponseEntity.ok(responseVO);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "用户名或密码无效"));
        }
    }

    @GetMapping("/user/role")
    public ResponseEntity<Map<String, String>> getUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
             return ResponseEntity.status(401).body(Map.of("error", "用户未登录"));
        }
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(Object::toString)
                .orElse("N/A");
        return ResponseEntity.ok(Map.of("role", role));
    }

    // 注意：登出由Spring Security的LogoutFilter处理，所以不需要/logout的Controller
    // 如果需要保留，确保它不会和SecurityConfig中的logout配置冲突
    @PutMapping("/user/password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userMapper.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("error", "用户不存在"));
        }
        try {
            userService.changePassword(user.getUserId(), passwordChangeDTO.getCurrentPassword(), passwordChangeDTO.getNewPassword());
            return ResponseEntity.ok(Map.of("message", "密码修改成功"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }
}