package com.example.mapper;

import com.example.vo.ApprovalRecordVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApprovalRecordMapper {
    List<ApprovalRecordVO> getApprovalRecord();
}