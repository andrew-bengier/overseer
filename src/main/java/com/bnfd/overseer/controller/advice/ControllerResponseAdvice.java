package com.bnfd.overseer.controller.advice;

import com.bnfd.overseer.exception.*;
import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.http.converter.*;
import org.springframework.validation.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.*;
import org.springframework.web.servlet.mvc.method.annotation.*;

import java.util.*;

@Slf4j
@ControllerAdvice
public class ControllerResponseAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(OverseerBadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(OverseerBadRequestException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        return new ResponseEntity<>(ex.toString(), OverseerBadRequestException.getStatusCode());
    }

    @ExceptionHandler(OverseerConflictException.class)
    public ResponseEntity<Object> handleConflictException(OverseerConflictException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        return new ResponseEntity<>(ex.toString(), OverseerConflictException.getStatusCode());
    }

    @ExceptionHandler(OverseerNoContentException.class)
    public ResponseEntity<Object> handleNoContentException(OverseerNoContentException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        return new ResponseEntity<>(ex.toString(), OverseerNoContentException.getStatusCode());
    }

    @ExceptionHandler(OverseerNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(OverseerNotFoundException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        return new ResponseEntity<>(ex.toString(), OverseerNotFoundException.getStatusCode());
    }

    @ExceptionHandler(OverseerUnprocessableException.class)
    public ResponseEntity<Object> handleUnprocessableEntityException(OverseerUnprocessableException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        return new ResponseEntity<>(ex.toString(), OverseerUnprocessableException.getStatusCode());
    }

    @ExceptionHandler(OverseerPreConditionRequiredException.class)
    public ResponseEntity<Object> handlePreConditionRequiredException(OverseerPreConditionRequiredException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        return new ResponseEntity<>(ex.toString(), OverseerPreConditionRequiredException.getStatusCode());
    }

    @ExceptionHandler(OverseerException.class)
    public ResponseEntity<Object> handleProcessingException(OverseerException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        return new ResponseEntity<>(ex.toString(), OverseerException.getStatusCode());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleGenericException(RuntimeException ex, WebRequest request) {
        log.error("Unexpected Service Exception.", ex);

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Unexpected Service Exception.");
        body.put("errors", List.of(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // region - Overridden Methods -
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> body = new HashMap<>();

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.error("Provided request object has failed validation - %s".formatted(errors.toString()));

        body.put("message", "Invalid Request Object - Fields: %s".formatted(errors.toString()));
        body.put("errors", List.of(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase()));
        body.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());

        return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("Provided request could not be parsed", ex);

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Invalid Request Object - Not Parseable");
        body.put("errors", List.of(HttpStatus.BAD_REQUEST.getReasonPhrase()));
        body.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    // endregion - Overridden Methods -
}
