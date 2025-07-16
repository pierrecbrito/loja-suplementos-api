package com.suplementos.lojasuplementosapi.service;

import com.suplementos.lojasuplementosapi.domain.Carrinho;
import com.suplementos.lojasuplementosapi.domain.ItemCarrinho;
import com.suplementos.lojasuplementosapi.domain.Suplemento;
import com.suplementos.lojasuplementosapi.domain.Usuario;
import com.suplementos.lojasuplementosapi.dto.CarrinhoResponse;
import com.suplementos.lojasuplementosapi.dto.ItemCarrinhoRequest;
import com.suplementos.lojasuplementosapi.mapper.CarrinhoMapper;
import com.suplementos.lojasuplementosapi.repository.CarrinhoRepository;
import com.suplementos.lojasuplementosapi.repository.ItemCarrinhoRepository;
import com.suplementos.lojasuplementosapi.repository.SuplementoRepository;
import com.suplementos.lojasuplementosapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CarrinhoService {
    
    private final CarrinhoRepository carrinhoRepository;
    private final ItemCarrinhoRepository itemCarrinhoRepository;
    private final SuplementoRepository suplementoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CarrinhoMapper carrinhoMapper;
    
    @Transactional(readOnly = true)
    public CarrinhoResponse obterCarrinhoPorUsuario(Long usuarioId) {
        log.info("Buscando carrinho do usuário: {}", usuarioId);
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        Carrinho carrinho = carrinhoRepository.findByUsuarioIdWithItems(usuarioId)
                .orElseGet(() -> criarCarrinhoVazio(usuario));
        
        return carrinhoMapper.toResponse(carrinho);
    }
    
    public CarrinhoResponse adicionarItem(Long usuarioId, ItemCarrinhoRequest request) {
        log.info("Adicionando item ao carrinho do usuário: {}", usuarioId);
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        Suplemento suplemento = suplementoRepository.findById(request.getSuplementoId())
                .orElseThrow(() -> new RuntimeException("Suplemento não encontrado"));
        
        // Verifica se há estoque suficiente
        if (suplemento.getQuantidadeEstoque() < request.getQuantidade()) {
            throw new RuntimeException("Estoque insuficiente para o produto");
        }
        
        // Busca ou cria o carrinho do usuário
        Carrinho carrinho = carrinhoRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> criarCarrinhoVazio(usuario));
        
        // Verifica se o item já existe no carrinho
        Optional<ItemCarrinho> itemExistente = itemCarrinhoRepository
                .findByCarrinhoIdAndSuplementoId(carrinho.getId(), suplemento.getId());
        
        if (itemExistente.isPresent()) {
            // Se existe, atualiza a quantidade
            ItemCarrinho item = itemExistente.get();
            int novaQuantidade = item.getQuantidade() + request.getQuantidade();
            
            // Verifica se a nova quantidade não excede o estoque
            if (novaQuantidade > suplemento.getQuantidadeEstoque()) {
                throw new RuntimeException("Quantidade total excede o estoque disponível");
            }
            
            item.setQuantidade(novaQuantidade);
            itemCarrinhoRepository.save(item);
        } else {
            // Se não existe, cria novo item
            ItemCarrinho novoItem = new ItemCarrinho();
            novoItem.setCarrinho(carrinho);
            novoItem.setSuplemento(suplemento);
            novoItem.setQuantidade(request.getQuantidade());
            novoItem.setPrecoUnitario(suplemento.getPreco());
            
            itemCarrinhoRepository.save(novoItem);
        }
        
        // Atualiza o valor total do carrinho
        carrinho.calcularValorTotal();
        carrinhoRepository.save(carrinho);
        
        return carrinhoMapper.toResponse(carrinho);
    }
    
    public CarrinhoResponse removerItem(Long usuarioId, Long itemId) {
        log.info("Removendo item {} do carrinho do usuário: {}", itemId, usuarioId);
        
        ItemCarrinho item = itemCarrinhoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));
        
        // Verifica se o item pertence ao carrinho do usuário
        if (!item.getCarrinho().getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("Item não pertence ao usuário");
        }
        
        Carrinho carrinho = item.getCarrinho();
        itemCarrinhoRepository.delete(item);
        
        // Atualiza o valor total do carrinho
        carrinho.calcularValorTotal();
        carrinhoRepository.save(carrinho);
        
        return carrinhoMapper.toResponse(carrinho);
    }
    
    public CarrinhoResponse atualizarQuantidadeItem(Long usuarioId, Long itemId, Integer novaQuantidade) {
        log.info("Atualizando quantidade do item {} para {} no carrinho do usuário: {}", 
                itemId, novaQuantidade, usuarioId);
        
        if (novaQuantidade <= 0) {
            throw new RuntimeException("Quantidade deve ser maior que zero");
        }
        
        ItemCarrinho item = itemCarrinhoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));
        
        // Verifica se o item pertence ao carrinho do usuário
        if (!item.getCarrinho().getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("Item não pertence ao usuário");
        }
        
        // Verifica se há estoque suficiente
        if (item.getSuplemento().getQuantidadeEstoque() < novaQuantidade) {
            throw new RuntimeException("Estoque insuficiente para o produto");
        }
        
        item.setQuantidade(novaQuantidade);
        itemCarrinhoRepository.save(item);
        
        // Atualiza o valor total do carrinho
        Carrinho carrinho = item.getCarrinho();
        carrinho.calcularValorTotal();
        carrinhoRepository.save(carrinho);
        
        return carrinhoMapper.toResponse(carrinho);
    }
    
    public void limparCarrinho(Long usuarioId) {
        log.info("Limpando carrinho do usuário: {}", usuarioId);
        
        Carrinho carrinho = carrinhoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));
        
        itemCarrinhoRepository.deleteByCarrinhoId(carrinho.getId());
        
        // Atualiza o valor total do carrinho
        carrinho.calcularValorTotal();
        carrinhoRepository.save(carrinho);
    }
    
    private Carrinho criarCarrinhoVazio(Usuario usuario) {
        log.info("Criando novo carrinho para o usuário: {}", usuario.getId());
        
        Carrinho carrinho = new Carrinho();
        carrinho.setUsuario(usuario);
        carrinho.setValorTotal(BigDecimal.ZERO);
        
        return carrinhoRepository.save(carrinho);
    }
}
