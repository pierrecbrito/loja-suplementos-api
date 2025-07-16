package com.suplementos.lojasuplementosapi.service;

import com.suplementos.lojasuplementosapi.core.ApiConstants;
import com.suplementos.lojasuplementosapi.domain.ItemPedido;
import com.suplementos.lojasuplementosapi.domain.Pedido;
import com.suplementos.lojasuplementosapi.domain.Suplemento;
import com.suplementos.lojasuplementosapi.domain.Usuario;
import com.suplementos.lojasuplementosapi.dto.PedidoRequest;
import com.suplementos.lojasuplementosapi.dto.PedidoResponse;
import com.suplementos.lojasuplementosapi.erroHandling.BadRequestException;
import com.suplementos.lojasuplementosapi.erroHandling.ResourceNotFoundException;
import com.suplementos.lojasuplementosapi.mapper.PedidoMapper;
import com.suplementos.lojasuplementosapi.repository.PedidoRepository;
import com.suplementos.lojasuplementosapi.repository.SuplementoRepository;
import com.suplementos.lojasuplementosapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SuplementoRepository suplementoRepository;
    private final PedidoMapper pedidoMapper;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository,
                        UsuarioRepository usuarioRepository,
                        SuplementoRepository suplementoRepository,
                        PedidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.suplementoRepository = suplementoRepository;
        this.pedidoMapper = pedidoMapper;
    }

    @Transactional(readOnly = true)
    public Page<PedidoResponse> findAll(Pageable pageable) {
        Page<Pedido> pedidos = pedidoRepository.findAll(pageable);
        return pedidos.map(pedidoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public PedidoResponse findById(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ApiConstants.ERRO_PEDIDO_NAO_ENCONTRADO + id));
        return pedidoMapper.toResponse(pedido);
    }

    @Transactional(readOnly = true)
    public Page<PedidoResponse> findByUsuario(Long usuarioId, Pageable pageable) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ApiConstants.ERRO_USUARIO_NAO_ENCONTRADO + usuarioId));
        
        Page<Pedido> pedidos = pedidoRepository.findByUsuarioAndActiveTrue(usuario, pageable);
        return pedidos.map(pedidoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<PedidoResponse> findByStatus(Pedido.StatusPedido status, Pageable pageable) {
        Page<Pedido> pedidos = pedidoRepository.findByStatusAndActiveTrue(status, pageable);
        return pedidos.map(pedidoMapper::toResponse);
    }

    public PedidoResponse create(PedidoRequest pedidoRequest, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ApiConstants.ERRO_USUARIO_NAO_ENCONTRADO + usuarioId));

        Pedido pedido = pedidoMapper.toEntity(pedidoRequest, usuario);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(Pedido.StatusPedido.PENDENTE);

        // Processar itens do pedido
        Set<ItemPedido> itens = new HashSet<>();
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (var itemRequest : pedidoRequest.getItens()) {
            Suplemento suplemento = suplementoRepository.findById(itemRequest.getSuplementoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            ApiConstants.ERRO_SUPLEMENTO_NAO_ENCONTRADO + itemRequest.getSuplementoId()));

            // Verificar estoque disponível
            if (suplemento.getQuantidadeEstoque() < itemRequest.getQuantidade()) {
                throw new BadRequestException("Estoque insuficiente para o suplemento: " + suplemento.getNome());
            }

            ItemPedido item = new ItemPedido();
            item.setSuplemento(suplemento);
            item.setQuantidade(itemRequest.getQuantidade());
            item.setPrecoUnitario(suplemento.getPreco());
            item.setPedido(pedido);

            itens.add(item);
            valorTotal = valorTotal.add(suplemento.getPreco().multiply(BigDecimal.valueOf(itemRequest.getQuantidade())));

            // Atualizar estoque
            suplemento.setQuantidadeEstoque(suplemento.getQuantidadeEstoque() - itemRequest.getQuantidade());
            suplementoRepository.save(suplemento);
        }

        pedido.setItens(itens);
        pedido.setValorTotal(valorTotal);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return pedidoMapper.toResponse(pedidoSalvo);
    }

    public PedidoResponse updateStatus(Long id, Pedido.StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ApiConstants.ERRO_PEDIDO_NAO_ENCONTRADO + id));

        // Validar transição de status
        if (!isValidStatusTransition(pedido.getStatus(), novoStatus)) {
            throw new BadRequestException("Transição de status inválida de " + 
                                        pedido.getStatus() + " para " + novoStatus);
        }

        pedido.setStatus(novoStatus);
        
        // Se cancelado, devolver itens ao estoque
        if (novoStatus == Pedido.StatusPedido.CANCELADO) {
            for (ItemPedido item : pedido.getItens()) {
                Suplemento suplemento = item.getSuplemento();
                suplemento.setQuantidadeEstoque(suplemento.getQuantidadeEstoque() + item.getQuantidade());
                suplementoRepository.save(suplemento);
            }
        }

        Pedido pedidoAtualizado = pedidoRepository.save(pedido);
        return pedidoMapper.toResponse(pedidoAtualizado);
    }

    public void delete(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ApiConstants.ERRO_PEDIDO_NAO_ENCONTRADO + id));
        
        // Só permite deletar pedidos pendentes
        if (pedido.getStatus() != Pedido.StatusPedido.PENDENTE) {
            throw new BadRequestException("Só é possível deletar pedidos pendentes");
        }
        
        // Devolver itens ao estoque
        for (ItemPedido item : pedido.getItens()) {
            Suplemento suplemento = item.getSuplemento();
            suplemento.setQuantidadeEstoque(suplemento.getQuantidadeEstoque() + item.getQuantidade());
            suplementoRepository.save(suplemento);
        }
        
        // Soft delete
        pedido.setActive(false);
        pedidoRepository.save(pedido);
    }

    private boolean isValidStatusTransition(Pedido.StatusPedido statusAtual, Pedido.StatusPedido novoStatus) {
        return switch (statusAtual) {
            case PENDENTE -> novoStatus == Pedido.StatusPedido.PAGO || 
                           novoStatus == Pedido.StatusPedido.CANCELADO;
            case PAGO -> novoStatus == Pedido.StatusPedido.ENVIADO || 
                        novoStatus == Pedido.StatusPedido.CANCELADO;
            case ENVIADO -> novoStatus == Pedido.StatusPedido.ENTREGUE;
            case ENTREGUE, CANCELADO -> false; // Estados finais
        };
    }
}
