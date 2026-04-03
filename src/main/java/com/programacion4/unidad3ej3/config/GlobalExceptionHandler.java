package com.programacion4.unidad3ej3.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.List;

import com.programacion4.unidad3ej3.config.exceptions.CustomException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BaseResponse<Object>> handleCustomException(
            CustomException ex,
            HttpServletRequest request) {

        BaseResponse<Object> response = BaseResponse.builder()
                .data(null)
                .message(ex.getMessage())
                .errors(ex.getErrors())
                .timestamp(Instant.now().toString())
                .status(ex.getStatus().value())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .toList();

        BaseResponse<Object> response = BaseResponse.builder()
                .data(null)
                .message("Error de validación")
                .errors(errors)
                .timestamp(Instant.now().toString())
                .status(400)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleGeneric(
            Exception ex,
            HttpServletRequest request) {

        BaseResponse<Object> response = BaseResponse.builder()
                .data(null)
                .message("Ocurrió un error inesperado")
                .errors(List.of("Contacte al administrador"))
                .timestamp(Instant.now().toString())
                .status(500)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.internalServerError().body(response);
    }
}