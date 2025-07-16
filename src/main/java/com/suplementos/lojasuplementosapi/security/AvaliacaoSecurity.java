package com.suplementos.lojasuplementosapi.security;

import com.suplementos.lojasuplementosapi.domain.Avaliacao;
import com.suplementos.lojasuplementosapi.repository.AvaliacaoRepository;
import com.suplementos.lojasuplementosapi.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("avaliacaoSecurity")
public class AvaliacaoSecurity {
    
    @Autowired
    private AvaliacaoRepository avaliacaoRepository;
    
    public boolean isAvaliacaoOwner(Long avaliacaoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetailsImpl)) {
            return false;
        }
        
        UserDetailsImpl userDetails = (UserDetailsImpl) principal;
        Avaliacao avaliacao = avaliacaoRepository.findById(avaliacaoId).orElse(null);
        
        return avaliacao != null && avaliacao.getUsuario().getId().equals(userDetails.getId());
    }
    
    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));
    }
    
    public boolean isAvaliacaoOwnerOrAdmin(Long avaliacaoId) {
        return isAdmin() || isAvaliacaoOwner(avaliacaoId);
    }
}
