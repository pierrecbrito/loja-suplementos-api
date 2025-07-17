package com.suplementos.lojasuplementosapi.dto;

import com.suplementos.lojasuplementosapi.domain.Usuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioUpdateRequest {
    
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;
    
    @Email(message = "Email deve ser válido")
    private String email;
    
    @Size(min = 6, max = 100, message = "Senha deve ter entre 6 e 100 caracteres")
    private String senha;
    
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter entre 10 e 11 dígitos")
    private String telefone;
    
    private Usuario.Role role;
    
    @Valid
    private EnderecoRequest endereco;
}
