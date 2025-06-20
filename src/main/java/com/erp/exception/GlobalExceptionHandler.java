package com.erp.exception;

import com.erp.domain.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    // IllegalStateException이 발생하면 이 메서드가 호출됨
    @ResponseBody
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.fail(ex.getMessage()));
    }

    // DTO 유효성 검증 실패
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.badRequest().body(ApiResponse.fail(errorMessage));
    }

//
//    // 모든 예외
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse<Void>> handleOtherExceptions(Exception ex) {
//        return ResponseEntity.internalServerError().body(ApiResponse.fail("서버 오류: " + ex.getMessage()));
//    }
}
