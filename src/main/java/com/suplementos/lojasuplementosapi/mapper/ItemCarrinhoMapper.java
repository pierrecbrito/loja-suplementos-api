package com.suplementos.lojasuplementosapi.mapper;

import com.suplementos.lojasuplementosapi.domain.ItemCarrinho;
import com.suplementos.lojasuplementosapi.dto.ItemCarrinhoResponse;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ItemCarrinhoMapper {
    
    public ItemCarrinhoResponse toResponse(ItemCarrinho itemCarrinho) {
        if (itemCarrinho == null) {
            return null;
        }
        
        return ItemCarrinhoResponse.builder()
                .id(itemCarrinho.getId())
                .suplementoId(itemCarrinho.getSuplemento().getId())
                .suplementoNome(itemCarrinho.getSuplemento().getNome())
                .suplementoMarca(itemCarrinho.getSuplemento().getMarca())
                .suplementoImagemUrl(itemCarrinho.getSuplemento().getImagemUrl())
                .quantidade(itemCarrinho.getQuantidade())
                .precoUnitario(itemCarrinho.getPrecoUnitario())
                .subtotal(itemCarrinho.calcularSubtotal())
                .build();
    }
    
    public Set<ItemCarrinhoResponse> toResponseSet(Set<ItemCarrinho> itens) {
        if (itens == null) {
            return null;
        }
        
        return itens.stream()
                .map(this::toResponse)
                .collect(Collectors.toSet());
    }
}
