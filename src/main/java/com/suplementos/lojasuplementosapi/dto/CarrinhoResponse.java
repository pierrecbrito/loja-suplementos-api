package com.suplementos.lojasuplementosapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarrinhoResponse {
    
    private Long id;
    private Long usuarioId;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private BigDecimal valorTotal;
    private Set<ItemCarrinhoResponse> itens;
    private Integer quantidadeItens;
}
