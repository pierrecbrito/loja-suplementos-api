package com.suplementos.lojasuplementosapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoResponse {
    
    private Long id;
    private Long usuarioId;
    private String nomeUsuario;
    private Long suplementoId;
    private String nomeSuplemento;
    private Integer nota;
    private String comentario;
    private LocalDateTime dataAvaliacao;
}