package com.example.service;
import com.example.dto.RegisterDTO;
import com.example.vo.LoginResponseVO;
import com.example.vo.UserVO;
import java.util.List;
public interface UserService {
    LoginResponseVO login(String username, String password);
    void changePassword(Integer userId, String currentPassword, String newPassword);
    void register(RegisterDTO registerDTO);
    List<UserVO> getAllUsers();
}