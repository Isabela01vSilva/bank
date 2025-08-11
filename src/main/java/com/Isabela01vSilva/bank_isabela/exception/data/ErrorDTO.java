package com.Isabela01vSilva.bank_isabela.exception.data;

import java.time.LocalDateTime;

public class ErrorDTO {
    private LocalDateTime timestamp;
    private Integer status;
    private String errpr;
    private String messagr;

    public ErrorDTO(Integer status, String errpr, String messagr) {
        this.errpr = errpr;
        this.messagr = messagr;
        this.timestamp = LocalDateTime.now();
        this.status = status;
    }
}
