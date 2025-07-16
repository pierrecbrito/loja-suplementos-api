package com.suplementos.lojasuplementosapi.repository;

import com.suplementos.lojasuplementosapi.base.BaseRepository;
import com.suplementos.lojasuplementosapi.domain.Avaliacao;
import com.suplementos.lojasuplementosapi.domain.Suplemento;
import com.suplementos.lojasuplementosapi.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvaliacaoRepository extends BaseRepository<Avaliacao> {
    
    Page<Avaliacao> findBySuplementoAndActiveTrue(Suplemento suplemento, Pageable pageable);
    
    Page<Avaliacao> findByUsuarioAndActiveTrue(Usuario usuario, Pageable pageable);
    
    Optional<Avaliacao> findBySuplementoAndUsuarioAndActiveTrue(Suplemento suplemento, Usuario usuario);
}