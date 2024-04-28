package com.fullstack.education.labpcp.controller;

import com.fullstack.education.labpcp.controller.dto.request.TurmaRequest;
import com.fullstack.education.labpcp.controller.dto.response.TurmaComListAlunosResponse;
import com.fullstack.education.labpcp.controller.dto.response.TurmaResponse;
import com.fullstack.education.labpcp.service.TurmaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/turmas")
public class TurmaController {

    private final TurmaService turmaService;

    @PostMapping
    public ResponseEntity<TurmaResponse> criarTurma(@RequestHeader(name = "Authorization") String token, @RequestBody TurmaRequest turmaRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(turmaService.criarTurma(turmaRequest, token.substring(7)));
    }


    @GetMapping("{id}")
    public ResponseEntity<TurmaComListAlunosResponse> buscarTurmaPorId(@PathVariable Long id, @RequestHeader(name = "Authorization") String token ){
        return ResponseEntity.status(HttpStatus.OK).body(turmaService.obterTurmaPorId(id, token.substring(7)));
    }

    @PutMapping("{id}")
    public ResponseEntity<TurmaResponse> atualizarTurma(@PathVariable Long id, @RequestHeader(name = "Authorization" )String token, @RequestBody TurmaRequest turmaRequest){
        return ResponseEntity.status(HttpStatus.OK).body(turmaService.atualizarTurma(id, turmaRequest,token.substring(7) ));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletarTurma(@PathVariable Long id, @RequestHeader(name = "Authorization" ) String token){
        turmaService.excluirTurma(id, token.substring(7) );
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<List<TurmaComListAlunosResponse>> listarTurmas(@RequestHeader(name = "Authorization" ) String token){
        return ResponseEntity.status(HttpStatus.OK).body(turmaService.listarTodosTurmas( token.substring(7)));
    }
}
