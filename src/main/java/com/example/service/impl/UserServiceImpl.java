package com.example.service.impl;

import com.example.ExceptionHandler.AuthException;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.example.vo.LoginResponseVO;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseVO login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthException(HttpStatus.UNAUTHORIZED, "用户名或密码无效");
        }

        LoginResponseVO responseVO = new LoginResponseVO();
        responseVO.setMessage("登录成功");
        responseVO.setUserId(user.getUserId());
        responseVO.setUsername(user.getUsername());
        responseVO.setRole(user.getRoleName());
        return responseVO;
    }

    @Override
    @Transactional
    public void changePassword(Integer userId, String currentPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new AuthException(HttpStatus.UNAUTHORIZED, "用户未登录或会话已过期");
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new AuthException(HttpStatus.BAD_REQUEST, "当前密码不正确");
        }

        String hashedNewPassword = passwordEncoder.encode(newPassword);
        userMapper.updatePassword(userId, hashedNewPassword);
    }
}