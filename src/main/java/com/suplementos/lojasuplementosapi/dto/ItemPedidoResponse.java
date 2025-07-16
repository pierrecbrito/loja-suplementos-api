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
public class ItemPedidoResponse {
    
    private Long id;
    private Long suplementoId;
    private String nomeSuplemento;
    private String marcaSuplemento;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;
}