package com.fullstack.education.labpcp.controller;


import com.fullstack.education.labpcp.controller.dto.request.CadastroUsuarioRequest;
import com.fullstack.education.labpcp.controller.dto.response.CadastroUsuarioResponse;
import com.fullstack.education.labpcp.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CadastroUsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/cadastros")
    public ResponseEntity<CadastroUsuarioResponse> novoCadastro(@RequestHeader(name = "Authorization") String token, @RequestBody CadastroUsuarioRequest cadastroUsuarioRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrarUsuario(cadastroUsuarioRequest, token.substring(7)));
    }
}
