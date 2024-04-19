package com.fullstack.education.labpcp.controller;

import com.fullstack.education.labpcp.controller.dto.request.MateriaRequest;
import com.fullstack.education.labpcp.controller.dto.response.MateriaResponse;
import com.fullstack.education.labpcp.service.MateriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/materias")
public class MateriaController {

    private final MateriaService materiaService;

    @PostMapping
    public ResponseEntity<MateriaResponse> criarMateria(@RequestHeader(name = "Authorization") String token, @RequestBody MateriaRequest materiaRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(materiaService.criarMateria(materiaRequest, token.substring(7)));
    }

    @GetMapping("{id}")
    public ResponseEntity<MateriaResponse> buscarMateriaPorId(@PathVariable Long id, @RequestHeader(name = "Authorization") String token ){
        return ResponseEntity.status(HttpStatus.OK).body(materiaService.obterMateriaPorId(id, token.substring(7)));
    }

    @PutMapping("{id}")
    public ResponseEntity<MateriaResponse> atualizarMateria(@PathVariable Long id, @RequestHeader(name = "Authorization" )String token, @RequestBody MateriaRequest materiaRequest){
        return ResponseEntity.status(HttpStatus.OK).body(materiaService.atualizarMateria(id, materiaRequest,token.substring(7) ));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletarMateria(@PathVariable Long id, @RequestHeader(name = "Authorization" ) String token){
        materiaService.excluirMateria(id, token.substring(7) );
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<List<MateriaResponse>> listarMaterias(@RequestHeader(name = "Authorization" ) String token){
        return ResponseEntity.status(HttpStatus.OK).body(materiaService.listarTodasMaterias( token.substring(7)));
    }
}
