package com.suplementos.lojasuplementosapi.mapper;

import com.suplementos.lojasuplementosapi.domain.LogAuditoria;
import com.suplementos.lojasuplementosapi.dto.LogAuditoriaResponse;
import org.springframework.stereotype.Component;

@Component
public class LogAuditoriaMapper {
    
    public LogAuditoriaResponse toResponse(LogAuditoria log) {
        if (log == null) {
            return null;
        }
        
        return LogAuditoriaResponse.builder()
                .id(log.getId())
                .tabela(log.getTabela())
                .operacao(log.getOperacao())
                .registroId(log.getRegistroId())
                .usuarioId(log.getUsuarioId())
                .dadosAnteriores(log.getDadosAnteriores())
                .dadosNovos(log.getDadosNovos())
                .timestamp(log.getTimestamp())
                .ipAddress(log.getIpAddress())
                .userAgent(log.getUserAgent())
                .build();
    }
}
