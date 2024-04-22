package com.fullstack.education.labpcp.controller;

import com.fullstack.education.labpcp.controller.dto.request.DocenteRequest;
import com.fullstack.education.labpcp.controller.dto.response.DocenteResponse;
import com.fullstack.education.labpcp.service.DocenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/docentes")
public class DocenteController {

    private final DocenteService docenteService;

    @PostMapping
    public ResponseEntity<DocenteResponse> criarDocente(@RequestHeader(name = "Authorization") String token, @RequestBody DocenteRequest docenteRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(docenteService.criarDocente(docenteRequest, token.substring(7)));
    }

    @GetMapping("{id}")
    public ResponseEntity<DocenteResponse> buscarDocentePorId(@PathVariable Long id, @RequestHeader(name = "Authorization") String token ){
        return ResponseEntity.status(HttpStatus.OK).body(docenteService.obterDocentePorId(id, token.substring(7)));
    }

    @PutMapping("{id}")
    public ResponseEntity<DocenteResponse> atualizarDocente(@PathVariable Long id, @RequestHeader(name = "Authorization" )String token, @RequestBody DocenteRequest docenteRequest){
        return ResponseEntity.status(HttpStatus.OK).body(docenteService.atualizarDocente(id, docenteRequest,token.substring(7) ));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletarDocente(@PathVariable Long id, @RequestHeader(name = "Authorization" ) String token){
        docenteService.excluirDocente(id, token.substring(7) );
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<List<DocenteResponse>> listarDocentes(@RequestHeader(name = "Authorization" ) String token){
        return ResponseEntity.status(HttpStatus.OK).body(docenteService.listarTodosDocentes( token.substring(7)));
    }
}


