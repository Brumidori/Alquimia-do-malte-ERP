package com.les.erp_alquimia_do_malte.service;

import com.les.erp_alquimia_do_malte.dto.request.LoginRequest;
import com.les.erp_alquimia_do_malte.dto.response.TokenResponse;
import com.les.erp_alquimia_do_malte.security.CustomUserDetails;
import com.les.erp_alquimia_do_malte.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public TokenResponse login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha())
        );
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String token = jwtService.gerarToken(userDetails);
        return new TokenResponse(
                token, "Bearer",
                userDetails.getUsuarioId(),
                userDetails.getUsername(),
                userDetails.getUsername(),
                userDetails.getTipo(),
                userDetails.getSetorId()
        );
    }
}
