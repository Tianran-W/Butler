package com.example.inventory.common.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleValidationExceptions() {
        // 创建模拟异常
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        
        // 模拟处理
        ResponseEntity<com.example.inventory.common.dto.ErrorResponse> response = 
            handler.handleValidationExceptions(ex);
        
        // 验证
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getCode());
    }

    @Test
    void handleResourceNotFoundException() {
        // Arrange
        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");
        WebRequest request = mock(WebRequest.class);
        
        // Act
        ResponseEntity<com.example.inventory.common.dto.ErrorResponse> response = 
            handler.handleResourceNotFoundException(ex);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource not found", response.getBody().getMessage());
    }

    @Test
    void handleIllegalArgumentException() {
        // Arrange
        IllegalArgumentException ex = new IllegalArgumentException("Invalid argument");
        
        // Act
        ResponseEntity<com.example.inventory.common.dto.ErrorResponse> response = 
            handler.handleIllegalArgumentException(ex);
        
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid argument", response.getBody().getMessage());
    }

    @Test
    void handleAllExceptions() {
        // Arrange
        Exception ex = new Exception("General error");
        
        // Act
        ResponseEntity<com.example.inventory.common.dto.ErrorResponse> response = 
            handler.handleAllExceptions(ex);
        
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("服务器内部错误"));
    }
}