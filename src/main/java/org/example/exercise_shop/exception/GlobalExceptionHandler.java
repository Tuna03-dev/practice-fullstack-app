package org.example.exercise_shop.exception;

import org.example.exercise_shop.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handlerException(Exception exception){
        ErrorResponse error = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ErrorResponse> handlerRuntimeException(RuntimeException exception){
        ErrorResponse error = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = ApplicationException.class)
    public ResponseEntity<ErrorResponse> handlerAppException(ApplicationException exception){
        ErrorCode errorCode = exception.getErrorCode();

        ErrorResponse error = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(InvalidTokenException exception) {
        ErrorResponse error = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())

                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerValidateException(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(enumKey);
        ErrorResponse error = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity.badRequest().body(error);

    }
}
