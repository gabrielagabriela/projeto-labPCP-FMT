package com.fullstack.education.labpcp.infra.exception.customException;

public class CampoAusenteException extends RuntimeException{
    public CampoAusenteException() {
    }

    public CampoAusenteException(String message) {
        super(message);
    }
}
