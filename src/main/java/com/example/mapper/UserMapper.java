package com.example.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.User;
import com.example.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
@Mapper
public interface UserMapper extends BaseMapper<User> {
    User findByUsername(@Param("username") String username);
    int updatePassword(@Param("userId") Integer userId, @Param("newPassword") String newPassword);
    List<UserVO> findAllUsersWithRole();
}