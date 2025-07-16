package com.suplementos.lojasuplementosapi.service;

import com.suplementos.lojasuplementosapi.core.ApiConstants;
import com.suplementos.lojasuplementosapi.domain.Categoria;
import com.suplementos.lojasuplementosapi.domain.Suplemento;
import com.suplementos.lojasuplementosapi.dto.SuplementoRequest;
import com.suplementos.lojasuplementosapi.dto.SuplementoResponse;
import com.suplementos.lojasuplementosapi.erroHandling.BadRequestException;
import com.suplementos.lojasuplementosapi.erroHandling.ResourceNotFoundException;
import com.suplementos.lojasuplementosapi.mapper.SuplementoMapper;
import com.suplementos.lojasuplementosapi.repository.CategoriaRepository;
import com.suplementos.lojasuplementosapi.repository.SuplementoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class SuplementoService {

    private final SuplementoRepository suplementoRepository;
    private final CategoriaRepository categoriaRepository;
    private final SuplementoMapper suplementoMapper;

    @Autowired
    public SuplementoService(SuplementoRepository suplementoRepository, 
                           CategoriaRepository categoriaRepository,
                           SuplementoMapper suplementoMapper) {
        this.suplementoRepository = suplementoRepository;
        this.categoriaRepository = categoriaRepository;
        this.suplementoMapper = suplementoMapper;
    }

    @Transactional(readOnly = true)
    public Page<SuplementoResponse> findAll(Pageable pageable) {
        Page<Suplemento> suplementos = suplementoRepository.findAll(pageable);
        return suplementos.map(suplementoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public SuplementoResponse findById(Long id) {
        Suplemento suplemento = suplementoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ApiConstants.ERRO_SUPLEMENTO_NAO_ENCONTRADO + id));
        return suplementoMapper.toResponse(suplemento);
    }

    @Transactional(readOnly = true)
    public Page<SuplementoResponse> findByCategoria(Long categoriaId, Pageable pageable) {
        Page<Suplemento> suplementos = suplementoRepository.findByCategoriaIdAndActiveTrue(categoriaId, pageable);
        return suplementos.map(suplementoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<SuplementoResponse> findByNomeContaining(String nome, Pageable pageable) {
        Page<Suplemento> suplementos = suplementoRepository.findByNomeContainingIgnoreCaseAndActiveTrue(nome, pageable);
        return suplementos.map(suplementoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<SuplementoResponse> findByMarca(String marca, Pageable pageable) {
        Page<Suplemento> suplementos = suplementoRepository.findByMarcaAndActiveTrue(marca, pageable);
        return suplementos.map(suplementoMapper::toResponse);
    }

    public SuplementoResponse create(SuplementoRequest suplementoRequest) {
        Suplemento suplemento = suplementoMapper.toEntity(suplementoRequest);
        
        // Associar categorias
        if (suplementoRequest.getCategoriasIds() != null && !suplementoRequest.getCategoriasIds().isEmpty()) {
            Set<Categoria> categorias = new HashSet<>();
            for (Long categoriaId : suplementoRequest.getCategoriasIds()) {
                Categoria categoria = categoriaRepository.findById(categoriaId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                ApiConstants.ERRO_CATEGORIA_NAO_ENCONTRADA + categoriaId));
                categorias.add(categoria);
            }
            suplemento.setCategorias(categorias);
        }

        Suplemento suplementoSalvo = suplementoRepository.save(suplemento);
        return suplementoMapper.toResponse(suplementoSalvo);
    }

    public SuplementoResponse update(Long id, SuplementoRequest suplementoRequest) {
        Suplemento suplemento = suplementoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ApiConstants.ERRO_SUPLEMENTO_NAO_ENCONTRADO + id));

        suplementoMapper.updateEntityFromRequest(suplemento, suplementoRequest);
        
        // Atualizar categorias
        if (suplementoRequest.getCategoriasIds() != null) {
            Set<Categoria> categorias = new HashSet<>();
            for (Long categoriaId : suplementoRequest.getCategoriasIds()) {
                Categoria categoria = categoriaRepository.findById(categoriaId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                ApiConstants.ERRO_CATEGORIA_NAO_ENCONTRADA + categoriaId));
                categorias.add(categoria);
            }
            suplemento.setCategorias(categorias);
        }

        Suplemento suplementoAtualizado = suplementoRepository.save(suplemento);
        return suplementoMapper.toResponse(suplementoAtualizado);
    }

    public void delete(Long id) {
        Suplemento suplemento = suplementoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ApiConstants.ERRO_SUPLEMENTO_NAO_ENCONTRADO + id));
        
        // Verificar se o suplemento possui avaliações ou está em pedidos
        if (!suplemento.getAvaliacoes().isEmpty()) {
            throw new BadRequestException("Não é possível excluir suplemento com avaliações associadas");
        }
        
        // Soft delete
        suplemento.setActive(false);
        suplementoRepository.save(suplemento);
    }

    @Transactional(readOnly = true)
    public boolean existsByCodigoBarras(String codigoBarras) {
        return suplementoRepository.findByCodigoBarras(codigoBarras).isPresent();
    }
}
