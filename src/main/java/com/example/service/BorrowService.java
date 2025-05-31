package com.example.service;

import com.example.entity.BorrowRequest;
import com.example.vo.MaterialVO;

import java.util.List;

public interface BorrowService {
    // 新增借用记录
    void addNewBorrow(BorrowRequest borrowRequest);
    // 查询用户借用的物资信息
    List<MaterialVO> findUserBorrowing(Integer user_id);
}