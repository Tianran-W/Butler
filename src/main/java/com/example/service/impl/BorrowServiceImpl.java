package com.example.service.impl;

import com.example.entity.BorrowRequest;
import com.example.mapper.BorrowMapper;
import com.example.service.BorrowService;
import com.example.vo.MaterialVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.List;

@Service
public class BorrowServiceImpl implements BorrowService {

    @Resource
    private BorrowMapper borrowMapper;

    @Override
    @Transactional
    public void addNewBorrow(BorrowRequest borrowRequest) {
        if (borrowRequest.getIs_expensive() == 0) {
            // 不贵重物品，插入使用记录并更新物资状态
            borrowMapper.insertUsageRecord(borrowRequest);
            borrowMapper.updateMaterialStatus(borrowRequest.getMaterial_id(), "已借出");
        } else {
            // 贵重物品，插入审批记录并更新物资状态
            borrowMapper.insertApprovalRecord(borrowRequest);
            borrowMapper.updateMaterialStatus(borrowRequest.getMaterial_id(), "审批中");
        }
    }

    @Override
    public List<MaterialVO> findUserBorrowing(Integer user_id) {
        return borrowMapper.findUserBorrowing(user_id);
    }
}