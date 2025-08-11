package com.Isabela01vSilva.bank_isabela.exception;

import com.Isabela01vSilva.bank_isabela.exception.data.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class SchedulingExceptionHandler {

    @ExceptionHandler(SchedulingNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleSchedulingNotFoundException(SchedulingNotFoundException ex) {
        ErrorDTO bodyError = new ErrorDTO(
                HttpStatus.NOT_FOUND.value(),
                "NotFound",
                ex.getMessage()
        );
        return new ResponseEntity<>(bodyError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleException(Exception ex) {
        ErrorDTO bodyError = new ErrorDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                ex.getMessage()
        );
        return new ResponseEntity<>(bodyError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
