package com.fullstack.education.labpcp.infra.exception;

public class CampoAusenteException extends RuntimeException{
    public CampoAusenteException() {
    }

    public CampoAusenteException(String message) {
        super(message);
    }
}
