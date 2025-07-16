package com.suplementos.lojasuplementosapi.repository;

import com.suplementos.lojasuplementosapi.base.BaseRepository;
import com.suplementos.lojasuplementosapi.domain.LogAuditoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogAuditoriaRepository extends BaseRepository<LogAuditoria> {
    
    /**
     * Busca logs por tabela
     */
    Page<LogAuditoria> findByTabela(String tabela, Pageable pageable);
    
    /**
     * Busca logs por usuário
     */
    Page<LogAuditoria> findByUsuarioId(Long usuarioId, Pageable pageable);
    
    /**
     * Busca logs por operação
     */
    Page<LogAuditoria> findByOperacao(LogAuditoria.TipoOperacao operacao, Pageable pageable);
    
    /**
     * Busca logs por período
     */
    @Query("SELECT l FROM LogAuditoria l WHERE l.timestamp BETWEEN :inicio AND :fim ORDER BY l.timestamp DESC")
    Page<LogAuditoria> findByPeriodo(@Param("inicio") LocalDateTime inicio, 
                                     @Param("fim") LocalDateTime fim, 
                                     Pageable pageable);
    
    /**
     * Busca logs por registro específico
     */
    List<LogAuditoria> findByTabelaAndRegistroIdOrderByTimestampDesc(String tabela, Long registroId);
    
    /**
     * Busca logs recentes
     */
    @Query("SELECT l FROM LogAuditoria l ORDER BY l.timestamp DESC")
    Page<LogAuditoria> findRecentes(Pageable pageable);
}
