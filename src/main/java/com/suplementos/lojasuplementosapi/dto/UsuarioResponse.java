package com.suplementos.lojasuplementosapi.dto;

import com.suplementos.lojasuplementosapi.domain.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
    
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private Usuario.Role role;
    private EnderecoResponse endereco;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}