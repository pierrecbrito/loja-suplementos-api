package com.suplementos.lojasuplementosapi.dto;

import com.suplementos.lojasuplementosapi.domain.LogAuditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogAuditoriaResponse {
    
    private Long id;
    private String tabela;
    private LogAuditoria.TipoOperacao operacao;
    private Long registroId;
    private Long usuarioId;
    private String dadosAnteriores;
    private String dadosNovos;
    private LocalDateTime timestamp;
    private String ipAddress;
    private String userAgent;
}
