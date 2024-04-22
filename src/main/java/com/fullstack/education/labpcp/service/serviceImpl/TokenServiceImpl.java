package com.fullstack.education.labpcp.service.serviceImpl;

import com.fullstack.education.labpcp.controller.dto.request.LoginRequest;
import com.fullstack.education.labpcp.controller.dto.response.LoginResponse;
import com.fullstack.education.labpcp.datasource.entity.UsuarioEntity;
import com.fullstack.education.labpcp.datasource.repository.UsuarioRepository;
import com.fullstack.education.labpcp.infra.exception.customException.BadRequestException;
import com.fullstack.education.labpcp.infra.exception.customException.CampoAusenteException;
import com.fullstack.education.labpcp.infra.exception.customException.NotFoundException;
import com.fullstack.education.labpcp.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {

    private final BCryptPasswordEncoder bCryptEncoder;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDencoder;
    private final UsuarioRepository usuarioRepository;

    private static long TEMPO_EXPIRACAO = 36000L;

    @Override
    public LoginResponse tokenLogin(@RequestBody LoginRequest loginRequest) {

        if (loginRequest.login() == null || loginRequest.senha() == null) {
            throw new CampoAusenteException("Os campos login e senha são obrigatórios");
        }

        UsuarioEntity usuarioEntity = usuarioRepository.findByLogin(loginRequest.login().toLowerCase()).orElseThrow(() -> new NotFoundException("Não há registro com este login"));


        if (!usuarioEntity.senhaCorreta(loginRequest, bCryptEncoder)) {
            throw new BadRequestException("Erro! Senha incorreta, tente novamente.");
        }

        Instant now = Instant.now();

        String scope = usuarioEntity.getPapel().getNome();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("projetoLabPCP")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(TEMPO_EXPIRACAO))
                .subject(usuarioEntity.getId().toString())
                .claim("scope", scope)
                .build();

        var valorJWT = jwtEncoder.encode(
                        JwtEncoderParameters.from(claims)
                )
                .getTokenValue();

        log.info("Usuário logado-> Envio de token com sucesso");
        return new LoginResponse(valorJWT, TEMPO_EXPIRACAO);
    }


    @Override
    public String buscaCampo(String token, String claim) {
        return jwtDencoder
                .decode(token)
                .getClaims()
                .get(claim)
                .toString();
    }


}
