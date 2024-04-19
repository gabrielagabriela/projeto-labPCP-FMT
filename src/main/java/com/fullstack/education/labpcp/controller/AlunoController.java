package com.fullstack.education.labpcp.controller;

import com.fullstack.education.labpcp.controller.dto.request.AlunoRequest;
import com.fullstack.education.labpcp.controller.dto.response.AlunoResponse;
import com.fullstack.education.labpcp.service.AlunoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    @PostMapping
    public ResponseEntity<AlunoResponse> criarAluno(@RequestHeader(name = "Authorization") String token, @RequestBody AlunoRequest alunoRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoService.criarAluno(alunoRequest, token.substring(7)));
    }

    @GetMapping("{id}")
    public ResponseEntity<AlunoResponse> buscarAlunoPorId(@PathVariable Long id, @RequestHeader(name = "Authorization") String token ){
        return ResponseEntity.status(HttpStatus.OK).body(alunoService.obterAlunoPorId(id, token.substring(7)));
    }

    //////////////novo - testar
    @PutMapping("{id}")
    public ResponseEntity<AlunoResponse> atualizarAluno(@PathVariable Long id, @RequestHeader(name = "Authorization" )String token, @RequestBody AlunoRequest alunoRequest){
        return ResponseEntity.status(HttpStatus.OK).body(alunoService.atualizarAluno(id, alunoRequest,token.substring(7) ));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletarAluno(@PathVariable Long id, @RequestHeader(name = "Authorization" ) String token){
        alunoService.excluirAluno(id, token.substring(7) );
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<List<AlunoResponse>> listarAlunos(@RequestHeader(name = "Authorization" ) String token){
        return ResponseEntity.status(HttpStatus.OK).body(alunoService.listarTodosAlunos( token.substring(7)));
    }
}


