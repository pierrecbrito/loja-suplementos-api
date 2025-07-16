package com.suplementos.lojasuplementosapi.mapper;


import com.suplementos.lojasuplementosapi.dto.CategoriaRequest;
import com.suplementos.lojasuplementosapi.dto.CategoriaResponse;
import com.suplementos.lojasuplementosapi.domain.Categoria;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CategoriaMapper {
    
    public Categoria toEntity(CategoriaRequest request) {
        if (request == null) {
            return null;
        }
        
        Categoria categoria = new Categoria();
        categoria.setNome(request.getNome());
        categoria.setDescricao(request.getDescricao());
        
        return categoria;
    }
    
    public void updateEntityFromRequest(Categoria categoria, CategoriaRequest request) {
        if (categoria == null || request == null) {
            return;
        }
        
        categoria.setNome(request.getNome());
        categoria.setDescricao(request.getDescricao());
    }
    
    public CategoriaResponse toResponse(Categoria categoria) {
        if (categoria == null) {
            return null;
        }
        
        return CategoriaResponse.builder()
                .id(categoria.getId())
                .nome(categoria.getNome())
                .descricao(categoria.getDescricao())
                .build();
    }
    
    public List<CategoriaResponse> toResponseList(List<Categoria> categorias) {
        if (categorias == null) {
            return List.of();
        }
        
        return categorias.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    public Set<CategoriaResponse> toResponseSet(Set<Categoria> categorias) {
        if (categorias == null) {
            return Set.of();
        }
        
        return categorias.stream()
                .map(this::toResponse)
                .collect(Collectors.toSet());
    }
}