package com.fullstack.education.labpcp.infra.exception;


import com.fullstack.education.labpcp.controller.dto.response.ErroResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handler(Exception e) {
        ErroResponse erro = ErroResponse.builder()
                .codigo("500")
                .mensagem(e.getMessage())
                .build();
        return ResponseEntity.status(500).body(erro);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handler(NotFoundException e) {
        ErroResponse erro = ErroResponse.builder()
                .codigo("404")
                .mensagem(e.getMessage())
                .build();
        return ResponseEntity.status(404).body(erro);
    }

    @ExceptionHandler(AcessoNaoAutorizadoException.class)
    public ResponseEntity<?> handler(AcessoNaoAutorizadoException e) {
        ErroResponse erro = ErroResponse.builder()
                .codigo("403")
                .mensagem(e.getMessage())
                .build();
        return ResponseEntity.status(403).body(erro);
    }

    @ExceptionHandler(RegistroExistenteException.class)
    public ResponseEntity<?> handler(RegistroExistenteException e) {
        ErroResponse erro = ErroResponse.builder()
                .codigo("409")
                .mensagem(e.getMessage())
                .build();
        return ResponseEntity.status(409).body(erro);
    }

    @ExceptionHandler(CampoAusenteException.class)
    public ResponseEntity<?> handler(CampoAusenteException e) {
        ErroResponse erro = ErroResponse.builder()
                .codigo("400")
                .mensagem(e.getMessage())
                .build();
        return ResponseEntity.status(400).body(erro);
    }
    @ExceptionHandler(UsuarioIncompativelException.class)
    public ResponseEntity<?> handler(UsuarioIncompativelException e) {
        ErroResponse erro = ErroResponse.builder()
                .codigo("400")
                .mensagem(e.getMessage())
                .build();
        return ResponseEntity.status(400).body(erro);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handler(BadRequestException e) {
        ErroResponse erro = ErroResponse.builder()
                .codigo("400")
                .mensagem(e.getMessage())
                .build();
        return ResponseEntity.status(400).body(erro);
    }
}