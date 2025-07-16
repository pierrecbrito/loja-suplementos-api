package com.suplementos.lojasuplementosapi.dto;

import com.suplementos.lojasuplementosapi.domain.Pedido;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequest {
    
    @NotNull(message = "Forma de pagamento é obrigatória")
    private Pedido.FormaPagamento formaPagamento;
    
    @NotEmpty(message = "Itens do pedido não podem estar vazios")
    @Valid
    private Set<ItemPedidoRequest> itens;
}