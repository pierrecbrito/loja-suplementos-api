package com.suplementos.lojasuplementosapi.security;

import com.suplementos.lojasuplementosapi.domain.Pedido;
import com.suplementos.lojasuplementosapi.repository.PedidoRepository;
import com.suplementos.lojasuplementosapi.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("pedidoSecurity")
public class PedidoSecurity {
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    public boolean isPedidoOwner(Long pedidoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetailsImpl)) {
            return false;
        }
        
        UserDetailsImpl userDetails = (UserDetailsImpl) principal;
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
        
        return pedido != null && pedido.getUsuario().getId().equals(userDetails.getId());
    }
    
    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));
    }
    
    public boolean isPedidoOwnerOrAdmin(Long pedidoId) {
        return isAdmin() || isPedidoOwner(pedidoId);
    }
}
