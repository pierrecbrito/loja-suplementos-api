package com.suplementos.lojasuplementosapi.repository;


import com.suplementos.lojasuplementosapi.base.BaseRepository;
import com.suplementos.lojasuplementosapi.domain.Suplemento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface SuplementoRepository extends BaseRepository<Suplemento> {
    
    Optional<Suplemento> findByCodigoBarras(String codigoBarras);
    
    Page<Suplemento> findByActiveTrue(Pageable pageable);
    
    List<Suplemento> findByMarca(String marca);
    
    Page<Suplemento> findByMarcaAndActiveTrue(String marca, Pageable pageable);
    
    Page<Suplemento> findByNomeContainingIgnoreCaseAndActiveTrue(String nome, Pageable pageable);
    
    @Query("SELECT s FROM Suplemento s JOIN s.categorias c WHERE c.id = :categoriaId AND s.active = true")
    Page<Suplemento> findByCategoriaIdAndActiveTrue(@Param("categoriaId") Long categoriaId, Pageable pageable);
    
    @Query("SELECT s FROM Suplemento s WHERE s.preco BETWEEN :precoMin AND :precoMax AND s.active = true")
    Page<Suplemento> findByPrecoBetweenAndActiveTrue(
            @Param("precoMin") BigDecimal precoMin, 
            @Param("precoMax") BigDecimal precoMax,
            Pageable pageable);
    
    Page<Suplemento> findByDestaqueTrue(Pageable pageable);
}