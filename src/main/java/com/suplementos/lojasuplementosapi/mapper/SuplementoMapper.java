package com.suplementos.lojasuplementosapi.mapper;

import com.suplementos.lojasuplementosapi.dto.SuplementoRequest;
import com.suplementos.lojasuplementosapi.dto.SuplementoResponse;
import com.suplementos.lojasuplementosapi.domain.Categoria;
import com.suplementos.lojasuplementosapi.domain.Suplemento;

import com.suplementos.lojasuplementosapi.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SuplementoMapper {
    
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;
    
    public Suplemento toEntity(SuplementoRequest request) {
        if (request == null) {
            return null;
        }
        
        Suplemento suplemento = new Suplemento();
        suplemento.setNome(request.getNome());
        suplemento.setMarca(request.getMarca());
        suplemento.setDescricao(request.getDescricao());
        suplemento.setPreco(request.getPreco());
        suplemento.setQuantidadeEstoque(request.getQuantidadeEstoque());
        suplemento.setPesoGramas(request.getPesoGramas());
        suplemento.setImagemUrl(request.getImagemUrl());
        suplemento.setCodigoBarras(request.getCodigoBarras());
        suplemento.setDataValidade(request.getDataValidade());
        suplemento.setDestaque(request.isDestaque());
        
        // Mapear categorias pelos IDs
        if (request.getCategoriasIds() != null && !request.getCategoriasIds().isEmpty()) {
            Set<Categoria> categorias = new HashSet<>();
            for (Long categoriaId : request.getCategoriasIds()) {
                categoriaRepository.findById(categoriaId).ifPresent(categorias::add);
            }
            suplemento.setCategorias(categorias);
        }
        
        return suplemento;
    }
    
    public void updateEntityFromRequest(Suplemento suplemento, SuplementoRequest request) {
        if (suplemento == null || request == null) {
            return;
        }
        
        suplemento.setNome(request.getNome());
        suplemento.setMarca(request.getMarca());
        suplemento.setDescricao(request.getDescricao());
        suplemento.setPreco(request.getPreco());
        suplemento.setQuantidadeEstoque(request.getQuantidadeEstoque());
        suplemento.setPesoGramas(request.getPesoGramas());
        suplemento.setImagemUrl(request.getImagemUrl());
        suplemento.setCodigoBarras(request.getCodigoBarras());
        suplemento.setDataValidade(request.getDataValidade());
        suplemento.setDestaque(request.isDestaque());
        
        // Atualizar categorias
        if (request.getCategoriasIds() != null) {
            Set<Categoria> categorias = new HashSet<>();
            for (Long categoriaId : request.getCategoriasIds()) {
                categoriaRepository.findById(categoriaId).ifPresent(categorias::add);
            }
            suplemento.setCategorias(categorias);
        }
    }
    
    public SuplementoResponse toResponse(Suplemento suplemento) {
        if (suplemento == null) {
            return null;
        }
        
        // Calcular média das avaliações
        Double mediaAvaliacoes = null;
        if (suplemento.getAvaliacoes() != null && !suplemento.getAvaliacoes().isEmpty()) {
            mediaAvaliacoes = suplemento.getAvaliacoes().stream()
                    .mapToInt(avaliacao -> avaliacao.getNota())
                    .average()
                    .orElse(0.0);
        }
        
        return SuplementoResponse.builder()
                .id(suplemento.getId())
                .nome(suplemento.getNome())
                .marca(suplemento.getMarca())
                .descricao(suplemento.getDescricao())
                .preco(suplemento.getPreco())
                .quantidadeEstoque(suplemento.getQuantidadeEstoque())
                .pesoGramas(suplemento.getPesoGramas())
                .imagemUrl(suplemento.getImagemUrl())
                .codigoBarras(suplemento.getCodigoBarras())
                .dataValidade(suplemento.getDataValidade())
                .destaque(suplemento.isDestaque())
                .categorias(categoriaMapper.toResponseSet(suplemento.getCategorias()))
                .mediaAvaliacoes(mediaAvaliacoes)
                .build();
    }
    
    public List<SuplementoResponse> toResponseList(List<Suplemento> suplementos) {
        if (suplementos == null) {
            return List.of();
        }
        
        return suplementos.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}