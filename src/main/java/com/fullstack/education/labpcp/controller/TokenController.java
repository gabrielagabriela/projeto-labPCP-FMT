package com.fullstack.education.labpcp.controller;

import com.fullstack.education.labpcp.controller.dto.request.LoginRequest;
import com.fullstack.education.labpcp.controller.dto.response.LoginResponse;
import com.fullstack.education.labpcp.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;
    private static long TEMPO_EXPIRACAO = 36000L;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> gerarTokenLogin(@RequestBody LoginRequest loginRequest) {

        LoginResponse loginResponse = tokenService.tokenLogin(loginRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(loginResponse);

    }
}
