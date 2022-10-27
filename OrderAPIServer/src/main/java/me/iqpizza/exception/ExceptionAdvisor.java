package me.iqpizza.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionAdvisor {

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<?> bindException(BindException exception) {
        log.error(Objects.requireNonNull(exception.getFieldError()).toString());
        return ResponseEntity.badRequest().body(exception.getFieldError());
    }
}
