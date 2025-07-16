package com.suplementos.lojasuplementosapi.security;

import com.suplementos.lojasuplementosapi.service.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("usuarioSecurity")
public class UsuarioSecurity {
    
    public boolean isUsuario(Long usuarioId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetailsImpl)) {
            return false;
        }
        
        UserDetailsImpl userDetails = (UserDetailsImpl) principal;
        return userDetails.getId().equals(usuarioId);
    }
    
    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));
    }
    
    public boolean isUsuarioOrAdmin(Long usuarioId) {
        return isAdmin() || isUsuario(usuarioId);
    }
}
