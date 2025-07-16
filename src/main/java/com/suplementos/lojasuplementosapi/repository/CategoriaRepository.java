package com.suplementos.lojasuplementosapi.repository;

import com.suplementos.lojasuplementosapi.base.BaseRepository;
import com.suplementos.lojasuplementosapi.domain.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends BaseRepository<Categoria> {
    
    Optional<Categoria> findByNome(String nome);
    
    boolean existsByNome(String nome);
    
    Page<Categoria> findByActiveTrue(Pageable pageable);
}