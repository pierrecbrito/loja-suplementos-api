package com.suplementos.lojasuplementosapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuplementoRequest {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;
    
    @NotBlank(message = "Marca é obrigatória")
    @Size(min = 2, max = 50, message = "Marca deve ter entre 2 e 50 caracteres")
    private String marca;
    
    private String descricao;
    
    @NotNull(message = "Preço é obrigatório")
    @Min(value = 0, message = "Preço deve ser maior ou igual a zero")
    private BigDecimal preco;
    
    @NotNull(message = "Quantidade em estoque é obrigatória")
    @Min(value = 0, message = "Quantidade em estoque deve ser não negativa")
    private Integer quantidadeEstoque;
    
    private Integer pesoGramas;
    
    private String imagemUrl;
    
    private String codigoBarras;
    
    private LocalDate dataValidade;
    
    private boolean destaque = false;
    
    private Set<Long> categoriasIds;
}