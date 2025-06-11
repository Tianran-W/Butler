package com.example.service;

import com.example.vo.LoginResponseVO;

public interface UserService {
    LoginResponseVO login(String username, String password);
    void changePassword(Integer userId, String currentPassword, String newPassword);
}