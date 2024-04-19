package com.fullstack.education.labpcp.controller;

import com.fullstack.education.labpcp.controller.dto.request.CursoRequest;
import com.fullstack.education.labpcp.controller.dto.response.CursoListaMateriaResponse;
import com.fullstack.education.labpcp.controller.dto.response.CursoResponse;
import com.fullstack.education.labpcp.service.CursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;

    @PostMapping
    public ResponseEntity<CursoResponse> criarCurso(@RequestHeader(name = "Authorization") String token, @RequestBody CursoRequest cursoRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.criarCurso(cursoRequest, token.substring(7)));
    }

    @GetMapping("{id}")
    public ResponseEntity<CursoResponse> buscarCursoPorId(@PathVariable Long id, @RequestHeader(name = "Authorization") String token ){
        return ResponseEntity.status(HttpStatus.OK).body(cursoService.obterCursoPorId(id, token.substring(7)));
    }

 
    @PutMapping("{id}")
    public ResponseEntity<CursoResponse> atualizarCurso(@PathVariable Long id, @RequestHeader(name = "Authorization" )String token, @RequestBody CursoRequest cursoRequest){
        return ResponseEntity.status(HttpStatus.OK).body(cursoService.atualizarCurso(id, cursoRequest,token.substring(7) ));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletarCurso(@PathVariable Long id, @RequestHeader(name = "Authorization" ) String token){
        cursoService.excluirCurso(id, token.substring(7) );
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<List<CursoResponse>> listarCursos(@RequestHeader(name = "Authorization" ) String token){
        return ResponseEntity.status(HttpStatus.OK).body(cursoService.listarTodosCursos( token.substring(7)));
    }

    @GetMapping("{id}/materias")
    public ResponseEntity<List<CursoListaMateriaResponse>> listaMateriasPorCurso(@PathVariable Long id,@RequestHeader(name = "Authorization" ) String token ){
        return ResponseEntity.status(HttpStatus.OK).body(cursoService.listarMateriasPorCurso(id, token.substring(7)));
    }
}


