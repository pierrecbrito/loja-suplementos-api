package com.suplementos.lojasuplementosapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarrinhoResponse {
    
    private Long id;
    private Long suplementoId;
    private String suplementoNome;
    private String suplementoMarca;
    private String suplementoImagemUrl;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;
}
