package com.fullstack.education.labpcp.infra.exception.customException;

public class AcessoNaoAutorizadoException extends RuntimeException{
    public AcessoNaoAutorizadoException() {
    }

    public AcessoNaoAutorizadoException(String message) {
        super(message);
    }
}
