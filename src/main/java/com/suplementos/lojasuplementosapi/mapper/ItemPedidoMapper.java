package com.suplementos.lojasuplementosapi.mapper;

import com.suplementos.lojasuplementosapi.dto.ItemPedidoRequest;
import com.suplementos.lojasuplementosapi.dto.ItemPedidoResponse;
import com.suplementos.lojasuplementosapi.domain.ItemPedido;
import com.suplementos.lojasuplementosapi.domain.Pedido;
import com.suplementos.lojasuplementosapi.domain.Suplemento;
import org.springframework.stereotype.Component;

@Component
public class ItemPedidoMapper {
    
    public ItemPedido toEntity(ItemPedidoRequest request, Pedido pedido, Suplemento suplemento) {
        if (request == null) {
            return null;
        }
        
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setPedido(pedido);
        itemPedido.setSuplemento(suplemento);
        itemPedido.setQuantidade(request.getQuantidade());
        itemPedido.setPrecoUnitario(suplemento.getPreco());
        
        return itemPedido;
    }
    
    public ItemPedidoResponse toResponse(ItemPedido itemPedido) {
        if (itemPedido == null) {
            return null;
        }
        
        return ItemPedidoResponse.builder()
                .id(itemPedido.getId())
                .suplementoId(itemPedido.getSuplemento().getId())
                .nomeSuplemento(itemPedido.getSuplemento().getNome())
                .marcaSuplemento(itemPedido.getSuplemento().getMarca())
                .quantidade(itemPedido.getQuantidade())
                .precoUnitario(itemPedido.getPrecoUnitario())
                .subtotal(itemPedido.getSubtotal())
                .build();
    }
}