package com.suplementos.lojasuplementosapi.mapper;

import com.suplementos.lojasuplementosapi.dto.PedidoRequest;
import com.suplementos.lojasuplementosapi.dto.PedidoResponse;
import com.suplementos.lojasuplementosapi.dto.ItemPedidoRequest;
import com.suplementos.lojasuplementosapi.dto.ItemPedidoResponse;
import com.suplementos.lojasuplementosapi.domain.ItemPedido;
import com.suplementos.lojasuplementosapi.domain.Pedido;
import com.suplementos.lojasuplementosapi.domain.Suplemento;
import com.suplementos.lojasuplementosapi.domain.Usuario;
import com.suplementos.lojasuplementosapi.erroHandling.ResourceNotFoundException;
import com.suplementos.lojasuplementosapi.repository.SuplementoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PedidoMapper {
    
    private final SuplementoRepository suplementoRepository;
    private final ItemPedidoMapper itemPedidoMapper;
    
    public Pedido toEntity(PedidoRequest request, Usuario usuario) {
        if (request == null) {
            return null;
        }
        
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(Pedido.StatusPedido.PENDENTE);
        pedido.setFormaPagamento(request.getFormaPagamento());
        
        // Converter itens do pedido
        Set<ItemPedido> itens = new HashSet<>();
        for (ItemPedidoRequest itemRequest : request.getItens()) {
            Suplemento suplemento = suplementoRepository.findById(itemRequest.getSuplementoId())
                .orElseThrow(() -> new ResourceNotFoundException("Suplemento", "id", itemRequest.getSuplementoId()));
            
            ItemPedido item = itemPedidoMapper.toEntity(itemRequest, pedido, suplemento);
            itens.add(item);
        }
        
        pedido.setItens(itens);
        pedido.calcularValorTotal();
        
        return pedido;
    }
    
    public PedidoResponse toResponse(Pedido pedido) {
        if (pedido == null) {
            return null;
        }
        
        Set<ItemPedidoResponse> itensResponse = pedido.getItens().stream()
                .map(itemPedidoMapper::toResponse)
                .collect(Collectors.toSet());
        
        return PedidoResponse.builder()
                .id(pedido.getId())
                .usuarioId(pedido.getUsuario().getId())
                .nomeUsuario(pedido.getUsuario().getNome())
                .dataPedido(pedido.getDataPedido())
                .status(pedido.getStatus())
                .valorTotal(pedido.getValorTotal())
                .formaPagamento(pedido.getFormaPagamento())
                .itens(itensResponse)
                .build();
    }
}