package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.ExceptionHandler.AuthException;
import com.example.dto.RegisterDTO;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.example.vo.LoginResponseVO;
import jakarta.annotation.Resource;
import org.springframework.dao.DuplicateKeyException;
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

    @Override
    @Transactional
    public void register(RegisterDTO registerDTO) {
        // 检查用户名是否已存在
        if (userMapper.selectOne(new QueryWrapper<User>().eq("username", registerDTO.getUsername())) != null) {
            throw new DuplicateKeyException("用户名 '" + registerDTO.getUsername() + "' 已存在");
        }
        // 检查邮箱是否已存在
        if (userMapper.selectOne(new QueryWrapper<User>().eq("email", registerDTO.getEmail())) != null) {
            throw new DuplicateKeyException("邮箱 '" + registerDTO.getEmail() + "' 已被注册");
        }

        User newUser = new User();
        newUser.setUsername(registerDTO.getUsername());
        newUser.setEmail(registerDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        newUser.setRoleId(2);

        userMapper.insert(newUser);
    }
}