package com.suplementos.lojasuplementosapi.mapper;


import com.suplementos.lojasuplementosapi.dto.UsuarioRequest;
import com.suplementos.lojasuplementosapi.dto.UsuarioUpdateRequest;
import com.suplementos.lojasuplementosapi.dto.UsuarioResponse;
import com.suplementos.lojasuplementosapi.domain.Usuario;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UsuarioMapper {
    
    private final EnderecoMapper enderecoMapper;
    
    public Usuario toEntity(UsuarioRequest request) {
        if (request == null) {
            return null;
        }
        
        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(request.getSenha());
        usuario.setTelefone(request.getTelefone());
        usuario.setRole(request.getRole());
        
        if (request.getEndereco() != null) {
            usuario.setEndereco(enderecoMapper.toEntity(request.getEndereco()));
        }
        
        return usuario;
    }
    
    public void updateEntityFromRequest(Usuario usuario, UsuarioRequest request) {
        if (usuario == null || request == null) {
            return;
        }
        
        usuario.setNome(request.getNome());
        usuario.setTelefone(request.getTelefone());
        // Não atualizamos email e senha aqui, eles têm fluxos específicos
        
        if (request.getEndereco() != null) {
            if (usuario.getEndereco() != null) {
                enderecoMapper.updateEntityFromRequest(usuario.getEndereco(), request.getEndereco());
            } else {
                usuario.setEndereco(enderecoMapper.toEntity(request.getEndereco()));
            }
        }
    }
    
    public void updateEntityFromUpdateRequest(Usuario usuario, UsuarioUpdateRequest request) {
        if (usuario == null || request == null) {
            return;
        }
        
        // Atualiza apenas os campos que foram fornecidos (não são null)
        if (request.getNome() != null) {
            usuario.setNome(request.getNome());
        }
        
        if (request.getEmail() != null) {
            usuario.setEmail(request.getEmail());
        }
        
        if (request.getTelefone() != null) {
            usuario.setTelefone(request.getTelefone());
        }
        
        if (request.getRole() != null) {
            usuario.setRole(request.getRole());
        }
        
        if (request.getEndereco() != null) {
            if (usuario.getEndereco() != null) {
                enderecoMapper.updateEntityFromRequest(usuario.getEndereco(), request.getEndereco());
            } else {
                usuario.setEndereco(enderecoMapper.toEntity(request.getEndereco()));
            }
        }
    }
    
    public UsuarioResponse toResponse(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .telefone(usuario.getTelefone())
                .role(usuario.getRole())
                .endereco(usuario.getEndereco() != null ? enderecoMapper.toResponse(usuario.getEndereco()) : null)
                .createdAt(usuario.getCreatedAt())
                .updatedAt(usuario.getUpdatedAt())
                .build();
    }
    
    public List<UsuarioResponse> toResponseList(List<Usuario> usuarios) {
        if (usuarios == null) {
            return List.of();
        }
        
        return usuarios.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}