// ApprovalMapper.java
package com.example.inventory.approval.mapper;

import com.example.inventory.approval.entity.Approval;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApprovalMapper {
    int insert(Approval approval);
}
