package com.fullstack.education.labpcp.infra.exception.customException;

public class UsuarioIncompativelException extends RuntimeException{
    public UsuarioIncompativelException() {
    }

    public UsuarioIncompativelException(String message) {
        super(message);
    }
}
