package com.fullstack.education.labpcp.service.serviceImpl;


import com.fullstack.education.labpcp.controller.dto.request.CadastroUsuarioRequest;
import com.fullstack.education.labpcp.controller.dto.response.CadastroUsuarioResponse;
import com.fullstack.education.labpcp.datasource.entity.UsuarioEntity;
import com.fullstack.education.labpcp.datasource.repository.PapelRepository;
import com.fullstack.education.labpcp.datasource.repository.UsuarioRepository;
import com.fullstack.education.labpcp.service.TokenService;
import com.fullstack.education.labpcp.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final BCryptPasswordEncoder bCryptEncoder;
    private final UsuarioRepository usuarioRepository;
    private final PapelRepository papelRepository;
    private final TokenService tokenService;

    public CadastroUsuarioResponse cadastrarUsuario(@RequestBody CadastroUsuarioRequest cadastroUsuarioRequest, String token) {


        String nomePapel = tokenService.buscaCampo(token, "scope");
        if(!Objects.equals(nomePapel, "ADM")){
            throw new RuntimeException("Apenas administrador pode realizar o cadastro de novos usuários");
        }


        Optional<UsuarioEntity> usuarioExistenteOptional = usuarioRepository.findByLogin(cadastroUsuarioRequest.login());
        if (usuarioExistenteOptional.isPresent()) {
            throw new RuntimeException("Este usuário já tem cadastro");
        }


        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setLogin(cadastroUsuarioRequest.login());
        usuario.setSenha(bCryptEncoder.encode(cadastroUsuarioRequest.senha()));
        usuario.setPapel(papelRepository.findByNome(cadastroUsuarioRequest.papel()).orElseThrow(() -> new RuntimeException("Perfil inválido")));

        usuarioRepository.save(usuario);

        return new CadastroUsuarioResponse(usuario.getId(), usuario.getLogin());
    }

    @Override
    public boolean existeUsuariosCadastrados() {
        return usuarioRepository.count() > 0;
    }
    public void cadastrarUsuarioPreDefinido(String login, String senha, String papel){
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setLogin(login);
        usuario.setSenha(bCryptEncoder.encode(senha));
        usuario.setPapel(papelRepository.findByNome(papel).orElseThrow(() -> new RuntimeException("Perfil inválido")));
        usuarioRepository.save(usuario);

    }

}
