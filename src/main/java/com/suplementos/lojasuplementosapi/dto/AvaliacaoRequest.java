package com.suplementos.lojasuplementosapi.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoRequest {
    
    @NotNull(message = "ID do suplemento é obrigatório")
    private Long suplementoId;
    
    @NotNull(message = "Nota é obrigatória")
    @Min(value = 1, message = "Nota deve ser pelo menos 1")
    @Max(value = 5, message = "Nota deve ser no máximo 5")
    private Integer nota;
    
    @Size(max = 1000, message = "Comentário deve ter no máximo 1000 caracteres")
    private String comentario;
}