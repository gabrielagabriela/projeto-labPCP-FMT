package com.fullstack.education.labpcp.service.serviceImpl;


import com.fullstack.education.labpcp.controller.dto.request.CadastroUsuarioRequest;
import com.fullstack.education.labpcp.controller.dto.response.CadastroUsuarioResponse;
import com.fullstack.education.labpcp.datasource.entity.UsuarioEntity;
import com.fullstack.education.labpcp.datasource.repository.PapelRepository;
import com.fullstack.education.labpcp.datasource.repository.UsuarioRepository;
import com.fullstack.education.labpcp.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final BCryptPasswordEncoder bCryptEncoder;
    private final UsuarioRepository usuarioRepository;
    private final PapelRepository papelRepository;

    public CadastroUsuarioResponse cadastrarUsuario(@RequestBody CadastroUsuarioRequest cadastroUsuarioRequest) {

        boolean cadastroExistente = usuarioRepository.findByLogin(cadastroUsuarioRequest.login()).isEmpty();
        if (cadastroExistente) {
            throw new RuntimeException("Este usuário já tem cadastro");
        }

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setLogin(cadastroUsuarioRequest.login());
        usuario.setSenha(bCryptEncoder.encode(cadastroUsuarioRequest.senha()));
        usuario.setPapel(papelRepository.findByNome(cadastroUsuarioRequest.papel()).orElseThrow(() -> new RuntimeException("Perfil inválido")));

        usuarioRepository.save(usuario);

        return new CadastroUsuarioResponse(usuario.getId(), usuario.getLogin());
    }

}
