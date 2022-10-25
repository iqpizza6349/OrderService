package me.iqpizza.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DomainException extends RuntimeException {

    private static final Long serialVersionUID = -6174033292930156L;
    private final int status;
    private final String message;

}
