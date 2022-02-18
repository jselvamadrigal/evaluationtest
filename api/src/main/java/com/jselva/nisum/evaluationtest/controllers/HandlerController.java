package com.jselva.nisum.evaluationtest.controllers;

import com.jselva.nisum.evaluationtest.data.exceptions.ApiException;
import com.jselva.nisum.evaluationtest.data.exceptions.AttemptAuthenticationException;
import com.jselva.nisum.evaluationtest.data.models.response.ErrorApi;
import com.jselva.nisum.evaluationtest.data.models.response.ResponseGenericApi;
import io.jsonwebtoken.security.SignatureException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class HandlerController extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.NOT_FOUND.equals(status)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(this.buildResponse("Ruta invalida", HttpStatus.NOT_FOUND));
        }

        return super.handleNoHandlerFoundException(ex, headers, status, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseGenericApi<Object>> handleAccessDenied(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(this.buildResponse("Acceso denegado", HttpStatus.FORBIDDEN));
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ResponseGenericApi<Object>> handleSignature(SignatureException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(this.buildResponse("Token invalido", HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(AttemptAuthenticationException.class)
    public ResponseEntity<ResponseGenericApi<Object>> handleJwtAttemptAuthentication(AttemptAuthenticationException e) {
        return ResponseEntity.status(e.getStatus())
                .body(this.buildResponse("No autenticado",
                        e.getStatus())
                );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(this.buildResponse(ex));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ResponseGenericApi<Object>> handleUsernameNotFound(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(this.buildResponse("Usiario invalido", HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseGenericApi<Object>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(this.buildResponse(
                        "Opción invalida", HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ResponseGenericApi<Object>> handleUserApiAction(ApiException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(this.buildResponse(e.getMessage(), e.getHttpStatus()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseGenericApi<Object>> handleConstraintViolation(ConstraintViolationException e) {
        HttpStatus status = HttpStatus.FORBIDDEN;

        return ResponseEntity.status(status).body(
                this.buildResponse("Acceso invalido", HttpStatus.FORBIDDEN));
    }

    private ResponseGenericApi<Object> buildResponse(String message, HttpStatus status) {
        ErrorApi error = new ErrorApi();
        error.setCode(status.value());
        error.setMessageError(message);

        ResponseGenericApi<Object> response = new ResponseGenericApi<>();
        response.setError(error);
        response.setData(message);

        return response;
    }

    private ResponseGenericApi<Object> buildResponse(MethodArgumentNotValidException e) {
        ResponseGenericApi<Object> response = new ResponseGenericApi<>();
        response.setData(e.getBindingResult().getAllErrors()
                .stream().map(
                        error -> new AbstractMap.SimpleEntry<>(
                                ((FieldError) error).getField(),
                                error.getDefaultMessage()
                        )
                ).collect(Collectors.toList()).toString()
        );

        return response;
    }
}