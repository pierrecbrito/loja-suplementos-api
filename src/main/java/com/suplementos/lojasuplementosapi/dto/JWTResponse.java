package com.suplementos.lojasuplementosapi.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JWTResponse {
    
    private String token;
    @Builder.Default
    private String tipo = "Bearer";
    private Long id;
    private String nome;
    private String email;
    private String role;
}