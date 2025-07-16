package com.suplementos.lojasuplementosapi.core;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Aspecto para registrar log das operações da aplicação.
 * Esta é uma implementação de logger em nível de aplicação que registra
 * chamadas de métodos e exceções lançadas.
 */
@Aspect
@Component
public class Logging {
    
    /**
     * Pointcut que corresponde a todos os repositórios, serviços e endpoints REST.
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
        // Método vazio, usado apenas para definir um pointcut
    }
    
    /**
     * Pointcut que corresponde a todas as classes no pacote da aplicação.
     */
    @Pointcut("within(com.suplementos..*)")
    public void applicationPackagePointcut() {
        // Método vazio, usado apenas para definir um pointcut
    }
    
    /**
     * Registra o tempo de execução dos métodos e os parâmetros.
     */
    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
        
        if (log.isDebugEnabled()) {
            log.debug("Entrando: {}() com argumentos = {}", 
                    joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        
        try {
            long start = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;
            
            if (log.isDebugEnabled()) {
                log.debug("Saindo: {}() com resultado = {}, executado em {}ms", 
                        joinPoint.getSignature().getName(), result, executionTime);
            }
            
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Argumento ilegal: {} em {}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getName());
            throw e;
        }
    }
    
    /**
     * Registra exceções lançadas pelos métodos.
     */
    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        Logger log = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
        
        log.error("Exceção em {}(): {} - Mensagem: {}", 
                joinPoint.getSignature().getName(), e.getClass().getName(), e.getMessage());
    }
}