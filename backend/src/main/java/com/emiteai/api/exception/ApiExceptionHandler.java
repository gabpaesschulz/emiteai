package com.emiteai.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(DuplicateCpfException.class)
    public ResponseEntity<ErrorResponse> duplicateCpf(DuplicateCpfException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(NotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException ex) {

        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::format)
                .toList();

        return build(HttpStatus.BAD_REQUEST, "Dados inválidos", details);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> generic(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno não-tratado");
    }


    private ResponseEntity<ErrorResponse> build(HttpStatus status,
                                                String message) {
        return build(status, message, null);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status,
                                                String message,
                                                List<String> details) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(status.value(), message, details));
    }

    private String format(FieldError fe) {
        return fe.getField() + ": " + fe.getDefaultMessage();
    }
}
