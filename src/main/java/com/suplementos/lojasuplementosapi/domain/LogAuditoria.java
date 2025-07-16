package com.suplementos.lojasuplementosapi.domain;

import com.suplementos.lojasuplementosapi.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs_auditoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogAuditoria extends BaseEntity {
    
    @Column(name = "tabela", nullable = false)
    private String tabela;
    
    @Column(name = "operacao", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoOperacao operacao;
    
    @Column(name = "registro_id")
    private Long registroId;
    
    @Column(name = "usuario_id")
    private Long usuarioId;
    
    @Column(name = "dados_anteriores", columnDefinition = "TEXT")
    private String dadosAnteriores;
    
    @Column(name = "dados_novos", columnDefinition = "TEXT")
    private String dadosNovos;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    @Column(name = "ip_address")
    private String ipAddress;
    
    @Column(name = "user_agent")
    private String userAgent;
    
    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
    
    public enum TipoOperacao {
        INSERT, UPDATE, DELETE, SELECT
    }
}
