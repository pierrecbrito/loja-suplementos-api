package com.suplementos.lojasuplementosapi.repository;

import com.suplementos.lojasuplementosapi.base.BaseRepository;
import com.suplementos.lojasuplementosapi.domain.ItemCarrinho;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemCarrinhoRepository extends BaseRepository<ItemCarrinho> {
    
    /**
     * Busca todos os itens de um carrinho específico
     */
    List<ItemCarrinho> findByCarrinhoId(Long carrinhoId);
    
    /**
     * Busca um item específico no carrinho
     */
    Optional<ItemCarrinho> findByCarrinhoIdAndSuplementoId(Long carrinhoId, Long suplementoId);
    
    /**
     * Remove todos os itens de um carrinho
     */
    void deleteByCarrinhoId(Long carrinhoId);
    
    /**
     * Conta quantos itens um carrinho tem
     */
    long countByCarrinhoId(Long carrinhoId);
    
    /**
     * Busca itens do carrinho com informações do suplemento
     */
    @Query("SELECT ic FROM ItemCarrinho ic JOIN FETCH ic.suplemento WHERE ic.carrinho.id = :carrinhoId")
    List<ItemCarrinho> findByCarrinhoIdWithSuplemento(@Param("carrinhoId") Long carrinhoId);
}
