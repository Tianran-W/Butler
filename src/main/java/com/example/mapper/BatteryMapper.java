package com.example.mapper;

import com.example.vo.BatteryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface BatteryMapper {
    /**
     * 查询所有未报废的电池信息
     */
    List<BatteryVO> findAllBatteries();

    /**
     * 根据物资ID查询单个电池的详细信息
     */
    BatteryVO findBatteryById(@Param("materialId") Integer materialId);

    /**
     * 根据分类名称查找分类ID
     */
    Integer findCategoryIdByName(@Param("categoryName") String categoryName);

    /**
     * 更新物资状态（用于报废操作）
     */
    void updateMaterialStatus(@Param("materialId") Integer materialId, @Param("status") String status);
}