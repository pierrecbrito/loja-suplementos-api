package com.suplementos.lojasuplementosapi.config;

import com.suplementos.lojasuplementosapi.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditoriaAspect {
    
    private final AuditoriaService auditoriaService;
    
    @AfterReturning(pointcut = "execution(* com.suplementos.lojasuplementosapi.service.*.create(..))", 
                   returning = "result")
    public void auditarCriacao(JoinPoint joinPoint, Object result) {
        try {
            String className = joinPoint.getTarget().getClass().getSimpleName();
            String tableName = className.replace("Service", "").toLowerCase();
            
            Long id = extrairId(result);
            
            auditoriaService.registrarInsert(tableName, id, result);
            
        } catch (Exception e) {
            log.error("Erro ao auditar criação: {}", e.getMessage());
        }
    }
    
    @AfterReturning(pointcut = "execution(* com.suplementos.lojasuplementosapi.service.*.update(..))", 
                   returning = "result")
    public void auditarAtualizacao(JoinPoint joinPoint, Object result) {
        try {
            String className = joinPoint.getTarget().getClass().getSimpleName();
            String tableName = className.replace("Service", "").toLowerCase();
            
            Long id = extrairId(result);
            
            auditoriaService.registrarUpdate(tableName, id, null, result);
            
        } catch (Exception e) {
            log.error("Erro ao auditar atualização: {}", e.getMessage());
        }
    }
    
    @Before("execution(* com.suplementos.lojasuplementosapi.service.*.delete(..))")
    public void auditarExclusao(JoinPoint joinPoint) {
        try {
            String className = joinPoint.getTarget().getClass().getSimpleName();
            String tableName = className.replace("Service", "").toLowerCase();
            
            Object[] args = joinPoint.getArgs();
            Long id = null;
            if (args.length > 0 && args[0] instanceof Long) {
                id = (Long) args[0];
            }
            
            auditoriaService.registrarDelete(tableName, id, null);
            
        } catch (Exception e) {
            log.error("Erro ao auditar exclusão: {}", e.getMessage());
        }
    }
    
    // @AfterReturning(pointcut = "execution(* com.suplementos.lojasuplementosapi.service.*.findById(..))", 
    //                returning = "result")
    public void auditarConsulta(JoinPoint joinPoint, Object result) {
        try {
            String className = joinPoint.getTarget().getClass().getSimpleName();
            String tableName = className.replace("Service", "").toLowerCase();
            
            Object[] args = joinPoint.getArgs();
            Long id = null;
            if (args.length > 0 && args[0] instanceof Long) {
                id = (Long) args[0];
            }
            
            auditoriaService.registrarSelect(tableName, id);
            
        } catch (Exception e) {
            log.error("Erro ao auditar consulta: {}", e.getMessage());
        }
    }
    
    private Long extrairId(Object obj) {
        if (obj == null) return null;
        
        try {
            var method = obj.getClass().getMethod("getId");
            Object result = method.invoke(obj);
            return result instanceof Long ? (Long) result : null;
        } catch (Exception e) {
            return null;
        }
    }
}
