package com.dms.exceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dms.dto.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
    // public ResponseEntity<ApiResponse<String>> handleValidation(MethodArgumentNotValidException ex) {
    //     String message = ex.getBindingResult().getFieldError().getDefaultMessage();
    //     return ResponseEntity.badRequest().body(new ApiResponse<>(message, null));
    // }

    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {

    Map<String, String> errorMap = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                FieldError::getDefaultMessage,
                (existing, replacement) -> existing + ", " + replacement
            ));

    ApiResponse<Object> response = ApiResponse.error(
        "VALIDATION_ERROR", 
        "Validation failed for fields: " + errorMap.keySet(), null
    );

    // Optional: You can also put error details in data field
    // response.setData(errorMap);   // if you want to send detailed errors

    return ResponseEntity.badRequest().body(response);
}
}