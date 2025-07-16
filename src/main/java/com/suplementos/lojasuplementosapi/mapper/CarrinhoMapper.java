package com.suplementos.lojasuplementosapi.mapper;

import com.suplementos.lojasuplementosapi.domain.Carrinho;
import com.suplementos.lojasuplementosapi.dto.CarrinhoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarrinhoMapper {
    
    private final ItemCarrinhoMapper itemCarrinhoMapper;
    
    public CarrinhoResponse toResponse(Carrinho carrinho) {
        if (carrinho == null) {
            return null;
        }
        
        return CarrinhoResponse.builder()
                .id(carrinho.getId())
                .usuarioId(carrinho.getUsuario().getId())
                .dataCriacao(carrinho.getDataCriacao())
                .dataAtualizacao(carrinho.getDataAtualizacao())
                .valorTotal(carrinho.getValorTotal())
                .itens(itemCarrinhoMapper.toResponseSet(carrinho.getItens()))
                .quantidadeItens(carrinho.getItens().size())
                .build();
    }
}
