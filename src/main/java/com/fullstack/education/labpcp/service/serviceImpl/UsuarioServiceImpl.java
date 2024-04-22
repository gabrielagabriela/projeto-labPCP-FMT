package com.fullstack.education.labpcp.service.serviceImpl;


import com.fullstack.education.labpcp.controller.dto.request.CadastroUsuarioRequest;
import com.fullstack.education.labpcp.controller.dto.response.CadastroUsuarioResponse;
import com.fullstack.education.labpcp.datasource.entity.UsuarioEntity;
import com.fullstack.education.labpcp.datasource.repository.PapelRepository;
import com.fullstack.education.labpcp.datasource.repository.UsuarioRepository;
import com.fullstack.education.labpcp.infra.exception.AcessoNaoAutorizadoException;
import com.fullstack.education.labpcp.infra.exception.CampoAusenteException;
import com.fullstack.education.labpcp.infra.exception.NotFoundException;
import com.fullstack.education.labpcp.infra.exception.RegistroExistenteException;
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

    @Override
    public CadastroUsuarioResponse cadastrarUsuario(@RequestBody CadastroUsuarioRequest cadastroUsuarioRequest, String token) {

        if(cadastroUsuarioRequest.login() == null || cadastroUsuarioRequest.senha() == null || cadastroUsuarioRequest.papel() == null){
            throw new CampoAusenteException("Os campos login, senha e papel são obrigatórios");
        }

        String nomePapel = tokenService.buscaCampo(token, "scope");
        if(!Objects.equals(nomePapel, "ADM")){
            throw new AcessoNaoAutorizadoException("Apenas administrador pode realizar o cadastro de novos usuários");
        }

        Optional<UsuarioEntity> usuarioExistenteOptional = usuarioRepository.findByLogin(cadastroUsuarioRequest.login().toLowerCase());
        if (usuarioExistenteOptional.isPresent()) {
            throw new RegistroExistenteException("Já existe usuário cadastrado com este login!");
        }


        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setLogin(cadastroUsuarioRequest.login().toLowerCase());
        usuario.setSenha(bCryptEncoder.encode(cadastroUsuarioRequest.senha()));
        usuario.setPapel(papelRepository.findByNome(cadastroUsuarioRequest.papel().toUpperCase())
                .orElseThrow(() -> new NotFoundException("Papel inválido! Não há papel com este nome!")));

        usuarioRepository.save(usuario);

        return new CadastroUsuarioResponse(usuario.getId(), usuario.getLogin());
    }

    @Override
    public boolean existeUsuariosCadastrados() {
        return usuarioRepository.count() > 0;
    }
    @Override
    public void cadastrarUsuarioPreDefinido(String login, String senha, String papel){
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setLogin(login);
        usuario.setSenha(bCryptEncoder.encode(senha));
        usuario.setPapel(papelRepository.findByNome(papel).orElseThrow(() -> new RuntimeException("Perfil inválido")));
        usuarioRepository.save(usuario);
    }

}
