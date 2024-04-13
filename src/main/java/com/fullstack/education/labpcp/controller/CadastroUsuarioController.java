package com.fullstack.education.labpcp.controller;


import com.fullstack.education.labpcp.controller.dto.request.CadastroUsuarioRequest;
import com.fullstack.education.labpcp.controller.dto.response.CadastroUsuarioResponse;
import com.fullstack.education.labpcp.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("cadastros")
public class CadastroUsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<CadastroUsuarioResponse> novoCadastro(@RequestBody CadastroUsuarioRequest cadastroUsuarioRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrarUsuario(cadastroUsuarioRequest));
    }
}
