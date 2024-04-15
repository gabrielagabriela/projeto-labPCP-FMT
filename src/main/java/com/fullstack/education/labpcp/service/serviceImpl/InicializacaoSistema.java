package com.fullstack.education.labpcp.service.serviceImpl;

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
        // Verifica se já existem usuários cadastrados
        if (!usuarioService.existeUsuariosCadastrados()) {
            // Se não existirem, cadastra os usuários pré-definidos
            cadastrarUsuariosPredefinidos();
        }
    }

    private void cadastrarUsuariosPredefinidos() {
        // Cadastra os usuários pré-definidos
        usuarioService.cadastrarUsuarioPreDefinido("admin", "admin", "ADM");
    }

}
