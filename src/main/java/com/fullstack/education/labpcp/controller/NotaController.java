package com.fullstack.education.labpcp.controller;


import com.fullstack.education.labpcp.controller.dto.request.NotaRequest;
import com.fullstack.education.labpcp.controller.dto.response.NotaResponse;
import com.fullstack.education.labpcp.service.NotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notas")
public class NotaController {

    private final NotaService notaService;

    @PostMapping
    public ResponseEntity<NotaResponse> criarNota(@RequestHeader(name = "Authorization") String token, @RequestBody NotaRequest notaRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(notaService.criarNota(notaRequest, token.substring(7)));
    }

    @GetMapping("{id}")
    public ResponseEntity<NotaResponse> buscarNotaPorId(@PathVariable Long id, @RequestHeader(name = "Authorization") String token ){
        return ResponseEntity.status(HttpStatus.OK).body(notaService.obterNotaPorId(id, token.substring(7)));
    }

    @PutMapping("{id}")
    public ResponseEntity<NotaResponse> atualizarNota(@PathVariable Long id, @RequestHeader(name = "Authorization" )String token, @RequestBody NotaRequest notaRequest){
        return ResponseEntity.status(HttpStatus.OK).body(notaService.atualizarNota(id, notaRequest,token.substring(7) ));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletarNota(@PathVariable Long id, @RequestHeader(name = "Authorization" ) String token){
        notaService.excluirNota(id, token.substring(7) );
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<List<NotaResponse>> listarNotas(@RequestHeader(name = "Authorization" ) String token){
        return ResponseEntity.status(HttpStatus.OK).body(notaService.listarTodasNotas( token.substring(7)));
    }
}
