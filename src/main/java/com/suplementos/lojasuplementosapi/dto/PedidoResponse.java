package com.suplementos.lojasuplementosapi.dto;

import com.suplementos.lojasuplementosapi.domain.Pedido;
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
public class PedidoResponse {
    
    private Long id;
    private Long usuarioId;
    private String nomeUsuario;
    private LocalDateTime dataPedido;
    private Pedido.StatusPedido status;
    private BigDecimal valorTotal;
    private Pedido.FormaPagamento formaPagamento;
    private Set<ItemPedidoResponse> itens;
}