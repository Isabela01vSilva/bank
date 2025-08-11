package com.Isabela01vSilva.bank_isabela.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SchedulingNotFoundException extends RuntimeException {
    public SchedulingNotFoundException(String message) {
        super(message);
    }
}
