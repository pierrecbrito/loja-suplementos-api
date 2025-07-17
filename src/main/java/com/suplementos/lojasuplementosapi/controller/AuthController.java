package com.suplementos.lojasuplementosapi.controller;

import com.suplementos.lojasuplementosapi.core.ApiConstants;
import com.suplementos.lojasuplementosapi.dto.LoginRequest;
import com.suplementos.lojasuplementosapi.dto.UsuarioRequest;
import com.suplementos.lojasuplementosapi.dto.JWTResponse;
import com.suplementos.lojasuplementosapi.dto.UsuarioResponse;
import com.suplementos.lojasuplementosapi.service.UsuarioService;
import com.suplementos.lojasuplementosapi.service.UserDetailsImpl;
import com.suplementos.lojasuplementosapi.security.JWTUtils;
// Temporariamente desabilitado para testar a API
/*
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
*/
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping(ApiConstants.AUTH_PATH)
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final JWTUtils jwtUtils;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, 
                          UsuarioService usuarioService, 
                          JWTUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.usuarioService = usuarioService;
        this.jwtUtils = jwtUtils;
    }
    
    @PostMapping("/login")
    public ResponseEntity<JWTResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getSenha()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()).get(0);
        
        return ResponseEntity.ok(JWTResponse.builder()
                .token(jwt)
                .id(userDetails.getId())
                .nome(userDetails.getNome())
                .email(userDetails.getEmail())
                .role(role)
                .build());
    }
    
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> registerUser(@Valid @RequestBody UsuarioRequest usuarioRequest) {
        UsuarioResponse usuarioResponse = usuarioService.create(usuarioRequest);
        return ResponseEntity.status(201).body(usuarioResponse);
    }
}