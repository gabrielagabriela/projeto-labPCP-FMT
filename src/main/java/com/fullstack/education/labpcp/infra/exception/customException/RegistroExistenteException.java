package com.fullstack.education.labpcp.infra.exception.customException;

public class RegistroExistenteException extends RuntimeException{
    public RegistroExistenteException() {
    }

    public RegistroExistenteException(String message) {
        super(message);
    }
}
