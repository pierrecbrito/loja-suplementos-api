package com.suplementos.lojasuplementosapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suplementos.lojasuplementosapi.domain.LogAuditoria;
import com.suplementos.lojasuplementosapi.repository.LogAuditoriaRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditoriaService {
    
    private final LogAuditoriaRepository logAuditoriaRepository;
    private final ObjectMapper objectMapper;
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrarOperacao(String tabela, LogAuditoria.TipoOperacao operacao, 
                                 Long registroId, Object dadosAnteriores, Object dadosNovos) {
        try {
            LogAuditoria logAuditoria = new LogAuditoria();
            logAuditoria.setTabela(tabela);
            logAuditoria.setOperacao(operacao);
            logAuditoria.setRegistroId(registroId);
            logAuditoria.setUsuarioId(obterUsuarioLogado());
            logAuditoria.setTimestamp(LocalDateTime.now());
            
            // Capturar informações da requisição HTTP
            ServletRequestAttributes attributes = 
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                logAuditoria.setIpAddress(obterIpAddress(request));
                logAuditoria.setUserAgent(request.getHeader("User-Agent"));
            }
            
            // Serializar dados para JSON
            if (dadosAnteriores != null) {
                logAuditoria.setDadosAnteriores(objectMapper.writeValueAsString(dadosAnteriores));
            }
            if (dadosNovos != null) {
                logAuditoria.setDadosNovos(objectMapper.writeValueAsString(dadosNovos));
            }
            
            logAuditoriaRepository.save(logAuditoria);
            
            log.info("Operação {} registrada para tabela {} - Registro ID: {}", 
                    operacao, tabela, registroId);
            
        } catch (Exception e) {
            log.error("Erro ao registrar auditoria: {}", e.getMessage(), e);
        }
    }
    
    public void registrarInsert(String tabela, Long registroId, Object dadosNovos) {
        registrarOperacao(tabela, LogAuditoria.TipoOperacao.INSERT, registroId, null, dadosNovos);
    }
    
    public void registrarUpdate(String tabela, Long registroId, Object dadosAnteriores, Object dadosNovos) {
        registrarOperacao(tabela, LogAuditoria.TipoOperacao.UPDATE, registroId, dadosAnteriores, dadosNovos);
    }
    
    public void registrarDelete(String tabela, Long registroId, Object dadosAnteriores) {
        registrarOperacao(tabela, LogAuditoria.TipoOperacao.DELETE, registroId, dadosAnteriores, null);
    }
    
    public void registrarSelect(String tabela, Long registroId) {
        registrarOperacao(tabela, LogAuditoria.TipoOperacao.SELECT, registroId, null, null);
    }
    
    private Long obterUsuarioLogado() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                return userDetails.getId();
            }
        } catch (Exception e) {
            log.debug("Não foi possível obter usuário logado: {}", e.getMessage());
        }
        return null;
    }
    
    private String obterIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}
