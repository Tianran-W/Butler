package com.example.LendAndReturn.Return.service.impl;

import com.example.LendAndReturn.Return.entity.ReturnRecord;
import com.example.LendAndReturn.Return.mapper.ReturnRecordMapper;
import com.example.LendAndReturn.Lend.entity.BorrowRecord;
import com.example.LendAndReturn.Lend.mapper.BorrowRecordMapper;
import com.example.inventory.material.service.MaterialService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional // 确保测试在事务中运行，测试完成后自动回滚
public class ReturnRecordServiceImplTest {

    @Mock
    private ReturnRecordMapper returnRecordMapper;

    @Mock
    private BorrowRecordMapper borrowRecordMapper;

    @Mock
    private MaterialService materialService;

    @InjectMocks
    private ReturnRecordServiceImpl returnRecordService;

    @Test
    public void testCreateReturnRecord_Success() {
        // 准备测试数据
        ReturnRecord record = new ReturnRecord();
        record.setBorrowRecordId(1L);
        record.setMaterialId(1L);
        record.setUserId(1L);

        // 设置mock行为
        when(returnRecordMapper.insert(record)).thenReturn(1);

        // 执行测试
        boolean result = returnRecordService.createReturnRecord(record);

        // 验证结果
        assertTrue(result);
        assertNotNull(record.getReturnTime());

        // 验证mock调用
        verify(returnRecordMapper, times(1)).insert(record);
    }

    @Test
    public void testCreateReturnRecord_Fail_MissingInfo() {
        // 准备测试数据（缺少必要字段）
        ReturnRecord record = new ReturnRecord();
        record.setBorrowRecordId(1L);

        // 执行测试
        boolean result = returnRecordService.createReturnRecord(record);

        // 验证结果
        assertFalse(result);

        // 验证mock未被调用
        verify(returnRecordMapper, never()).insert(any());
    }

    @Test
    public void testGetReturnRecordsByBorrowRecordId() {
        // 准备测试数据
        Long borrowRecordId = 1L;
        ReturnRecord record = new ReturnRecord();
        record.setId(1L);
        record.setBorrowRecordId(borrowRecordId);

        // 设置mock行为
        when(returnRecordMapper.selectByBorrowRecordId(borrowRecordId))
                .thenReturn(Collections.singletonList(record));

        // 执行测试
        List<ReturnRecord> result = returnRecordService.getReturnRecordsByBorrowRecordId(borrowRecordId);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(borrowRecordId, result.get(0).getBorrowRecordId());

        // 验证mock调用
        verify(returnRecordMapper, times(1)).selectByBorrowRecordId(borrowRecordId);
    }

    @Test
    public void testProcessReturn_Success() {
        // 准备测试数据
        Long recordId = 1L;
        String photoUrl = "http://example.com/photo.jpg";

        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setId(recordId);
        borrowRecord.setMaterialId(1L);

        Material material = new Material();
        material.setId(1L);
        material.setStatus("借出");
        material.setQuantity(10);

        // 设置mock行为
        when(borrowRecordMapper.selectById(recordId)).thenReturn(borrowRecord);
//        when(materialService.getById(1L)).thenReturn(material);
        when(returnRecordMapper.updateById(any())).thenReturn(1);

        // 执行测试
        boolean result = returnRecordService.processReturn(recordId, photoUrl);

        // 验证结果
        assertTrue(result);

        // 验证mock调用
        verify(borrowRecordMapper, times(1)).selectById(recordId);
//        verify(materialService, times(1)).getById(1L);
        verify(returnRecordMapper, times(1)).updateById(any());
    }

    @Test
    public void testProcessReturn_Fail_BorrowRecordNotFound() {
        // 准备测试数据
        Long recordId = 1L;
        String photoUrl = "http://example.com/photo.jpg";

        // 设置mock行为
        when(borrowRecordMapper.selectById(recordId)).thenReturn(null);

        // 执行测试
        boolean result = returnRecordService.processReturn(recordId, photoUrl);

        // 验证结果
        assertFalse(result);

        // 验证mock调用
        verify(borrowRecordMapper, times(1)).selectById(recordId);
//        verify(materialService, never()).getById(any());
        verify(returnRecordMapper, never()).updateById(any());
    }
}