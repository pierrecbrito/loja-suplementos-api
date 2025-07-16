package com.suplementos.lojasuplementosapi.repository;

import com.suplementos.lojasuplementosapi.base.BaseRepository;
import com.suplementos.lojasuplementosapi.domain.Carrinho;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarrinhoRepository extends BaseRepository<Carrinho> {
    
    /**
     * Busca o carrinho de um usuário específico
     */
    Optional<Carrinho> findByUsuarioId(Long usuarioId);
    
    /**
     * Verifica se um usuário já possui um carrinho
     */
    boolean existsByUsuarioId(Long usuarioId);
    
    /**
     * Busca carrinho com todos os itens carregados
     */
    @Query("SELECT c FROM Carrinho c LEFT JOIN FETCH c.itens WHERE c.usuario.id = :usuarioId")
    Optional<Carrinho> findByUsuarioIdWithItems(@Param("usuarioId") Long usuarioId);
}
