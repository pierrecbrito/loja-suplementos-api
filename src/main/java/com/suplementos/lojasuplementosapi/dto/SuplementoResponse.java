package com.suplementos.lojasuplementosapi.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuplementoResponse {
    
    private Long id;
    private String nome;
    private String marca;
    private String descricao;
    private BigDecimal preco;
    private Integer quantidadeEstoque;
    private Integer pesoGramas;
    private String imagemUrl;
    private String codigoBarras;
    private LocalDate dataValidade;
    private boolean destaque;
    private Set<CategoriaResponse> categorias;
    private Double mediaAvaliacoes;
}