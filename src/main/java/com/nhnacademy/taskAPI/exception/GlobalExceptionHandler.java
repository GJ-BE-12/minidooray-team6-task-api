package com.nhnacademy.taskAPI.exception;

import com.nhnacademy.taskAPI.dto.response.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 Not Found
    @ExceptionHandler({
            ProjectNotFoundException.class,
            ProjectMemberNotFoundException.class,
            TaskNotFoundException.class,
            CommentNotFoundException.class,
            MilestoneNotFoundException.class,
            TagNotFoundException.class
    })
    public ResponseEntity<ErrorResponseDto> handleNotFound(RuntimeException e) {
        ErrorResponseDto error = new ErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // 403 Forbidden
    @ExceptionHandler(MemberAccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDenied(MemberAccessDeniedException e) {
        ErrorResponseDto error = new ErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    // 409 Conflict
    @ExceptionHandler({
            MemberAlreadyExistsException.class,
            TagAlreadyExistsException.class,
            TaskTagAlreadyExistsException.class
    })
    public ResponseEntity<ErrorResponseDto> handleConflict(RuntimeException e) {
        ErrorResponseDto error = new ErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ErrorResponseDto error = new ErrorResponseDto(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 500 Internal Server Error
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleRuntimeException(RuntimeException e) {
        ErrorResponseDto error = new ErrorResponseDto("서버 내부 오류가 발생했습니다: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}