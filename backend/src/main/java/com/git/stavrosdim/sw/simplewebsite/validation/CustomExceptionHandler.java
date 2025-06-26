package com.git.stavrosdim.sw.simplewebsite.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.git.stavrosdim.sw.simplewebsite.validation.CustomErrorMessage.ErrorMessageType;
import com.git.stavrosdim.sw.simplewebsite.validation.CustomExceptions.EmptyRequestBodyException;
import com.git.stavrosdim.sw.simplewebsite.validation.CustomExceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class CustomExceptionHandler {

        private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<CustomErrorMessage> methodArgumentExceptionHandler(MethodArgumentNotValidException e,
                        HttpServletRequest request) {
                final var errors = e.getBindingResult().getFieldErrors().stream()
                                .map(error -> "'" + error.getField() + "' error. " + error.getDefaultMessage())
                                .toList();
                warningLogs(ErrorMessageType.INVALID_REQUEST, e, request);
                return ResponseEntity.badRequest()
                                .body(new CustomErrorMessage(ErrorMessageType.INVALID_REQUEST.getMessage(), errors));
        }

        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<CustomErrorMessage> methodArgumentTypeMismatchException(
                        MethodArgumentTypeMismatchException e, HttpServletRequest request) {
                warningLogs(ErrorMessageType.INVALID_PARAMETERS, e, request);
                return ResponseEntity.badRequest()
                                .body(new CustomErrorMessage(ErrorMessageType.INVALID_PARAMETERS.getMessage(),
                                                e.getMessage()));
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<CustomErrorMessage> constraintViolationExceptionHandler(ConstraintViolationException e,
                        HttpServletRequest request) {
                final var violations = e.getConstraintViolations().stream()
                                .map(violation -> violation.getMessage())
                                .toList();
                warningLogs(ErrorMessageType.INVALID_PARAMETERS, e, request);
                return ResponseEntity.badRequest()
                                .body(new CustomErrorMessage(ErrorMessageType.INVALID_PARAMETERS.getMessage(),
                                                violations));
        }

        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<CustomErrorMessage> userNotFoundException(UserNotFoundException e,
                        HttpServletRequest request) {
                warningLogs(ErrorMessageType.USER_NOT_FOUND, e, request);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new CustomErrorMessage(ErrorMessageType.USER_NOT_FOUND.getMessage(),
                                                e.getMessage()));
        }

        @ExceptionHandler(EmptyRequestBodyException.class)
        public ResponseEntity<CustomErrorMessage> emptyRequestBodyException(EmptyRequestBodyException e,
                        HttpServletRequest request) {
                warningLogs(ErrorMessageType.INVALID_REQUEST, e, request);
                return ResponseEntity.badRequest()
                                .body(new CustomErrorMessage(ErrorMessageType.INVALID_REQUEST.getMessage(),
                                                ErrorMessageType.EMPTY_BODY.getMessage()));
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<CustomErrorMessage> httpMessageNotReadableException(HttpMessageNotReadableException e,
                        HttpServletRequest request) {
                warningLogs(ErrorMessageType.INVALID_REQUEST, e, request);
                return ResponseEntity.badRequest()
                                .body(new CustomErrorMessage(ErrorMessageType.INVALID_REQUEST.getMessage(),
                                                e.getCause() instanceof InvalidFormatException
                                                                ? ErrorMessageType.INCOMPATIBLE_DATE_FORMAT.getMessage()
                                                                : e.getMessage()));
        }

        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
        public ResponseEntity<CustomErrorMessage> httpRequestMethodNotSupportedException(
                        HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
                warningLogs(ErrorMessageType.INVALID_METHOD, e, request);
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(
                                new CustomErrorMessage(ErrorMessageType.INVALID_METHOD.getMessage(), e.getMessage()));
        }

        @ExceptionHandler(NoResourceFoundException.class)
        public ResponseEntity<CustomErrorMessage> noResourceFoundException(NoResourceFoundException e,
                        HttpServletRequest request) {
                warningLogs(ErrorMessageType.RESOURCE_NOT_FOUND, e, request);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage(
                                ErrorMessageType.RESOURCE_NOT_FOUND.getMessage(), e.getMessage()));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<CustomErrorMessage> fallbackException(Exception e, HttpServletRequest request) {
                errorLogs(ErrorMessageType.UNEXPECTED_ERROR, e, request);
                return ResponseEntity.internalServerError().body(
                                new CustomErrorMessage(ErrorMessageType.UNEXPECTED_ERROR.getMessage(), e.getMessage()));
        }

        private void warningLogs(ErrorMessageType error, Exception e, HttpServletRequest request) {
                logger.warn("[{} {}]: {}. Reason: {}", request.getMethod(), request.getRequestURI(),
                                error.getMessage(),
                                e.getMessage() == null ? e : e.getMessage());
        }

        private void errorLogs(ErrorMessageType error, Exception e, HttpServletRequest request) {
                logger.error("[{} {}]: {}. Exception type: {}. Reason: {}", request.getMethod(),
                                request.getRequestURI(),
                                error.getMessage(), e.getClass().getSimpleName(),
                                e.getMessage() == null ? e : e.getMessage());
        }
}
