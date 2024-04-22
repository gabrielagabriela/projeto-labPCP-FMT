package com.fullstack.education.labpcp.infra.inicializacao;

import com.fullstack.education.labpcp.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InicializacaoSistema implements ApplicationRunner {

    private final UsuarioService usuarioService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!usuarioService.existeUsuariosCadastrados()) {
            cadastrarUsuariosPredefinidos();
        }
    }

    private void cadastrarUsuariosPredefinidos() {
        usuarioService.cadastrarUsuarioPreDefinido("admin", "admin", "ADM");
        usuarioService.cadastrarUsuarioPreDefinido("admin2", "admin2", "ADM");
    }

}
