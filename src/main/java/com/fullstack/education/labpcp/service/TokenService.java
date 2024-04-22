package com.fullstack.education.labpcp.service;


import com.fullstack.education.labpcp.controller.dto.request.LoginRequest;
import com.fullstack.education.labpcp.controller.dto.response.LoginResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface TokenService {
    LoginResponse tokenLogin(@RequestBody LoginRequest loginRequest);

    String buscaCampo(String token, String claim);
}

