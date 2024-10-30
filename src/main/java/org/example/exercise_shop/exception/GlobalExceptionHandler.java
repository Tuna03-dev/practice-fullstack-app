package org.example.exercise_shop.exception;



import lombok.extern.slf4j.Slf4j;

import org.example.exercise_shop.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException e){
        log.error("RuntimeException: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("An unexpected error occurred")
                        .build()
        );
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(AccessDeniedException e){
        log.error("AccessDeniedException: ", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                ApiResponse.builder()
                        .code(HttpStatus.FORBIDDEN.value())
                        .message("Access denied")
                        .build()
        );
    }


    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception e){
        log.error("Exception: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("An unexpected error occurred")
                        .build()
        );
    }


    @ExceptionHandler(value = ApplicationException.class)
    public ResponseEntity<ApiResponse<Object>> handleApplicationException(ApplicationException e){
        log.error("ApplicationException: ", e);
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.badRequest().body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("MethodArgumentNotValidException: ", e);
        List<String> errors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        return ResponseEntity.badRequest().body(
                ApiResponse.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("Validation error")
                        .data(errors)
                        .build()
        );
    }

    @ExceptionHandler(value = OutOfStockException.class)
    public ResponseEntity<ApiResponse<Object>> handleOutOfStockException(OutOfStockException e){
        log.error("OutOfStockException: ", e);
        return ResponseEntity.badRequest().body(
                ApiResponse.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .data(e.getOutOfStockProductsMessage())
                        .message(e.getMessage())
                        .build()
        );
    }

}
