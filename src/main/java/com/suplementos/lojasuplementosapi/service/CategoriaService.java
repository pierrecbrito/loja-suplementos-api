package com.suplementos.lojasuplementosapi.service;

import com.suplementos.lojasuplementosapi.core.ApiConstants;
import com.suplementos.lojasuplementosapi.domain.Categoria;
import com.suplementos.lojasuplementosapi.dto.CategoriaRequest;
import com.suplementos.lojasuplementosapi.dto.CategoriaResponse;
import com.suplementos.lojasuplementosapi.erroHandling.BadRequestException;
import com.suplementos.lojasuplementosapi.erroHandling.ResourceNotFoundException;
import com.suplementos.lojasuplementosapi.mapper.CategoriaMapper;
import com.suplementos.lojasuplementosapi.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository, 
                           CategoriaMapper categoriaMapper) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
    }

    @Transactional(readOnly = true)
    public Page<CategoriaResponse> findAll(Pageable pageable) {
        Page<Categoria> categorias = categoriaRepository.findAll(pageable);
        return categorias.map(categoriaMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public CategoriaResponse findById(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ApiConstants.ERRO_CATEGORIA_NAO_ENCONTRADA + id));
        return categoriaMapper.toResponse(categoria);
    }

    @Transactional(readOnly = true)
    public Page<CategoriaResponse> findByNomeContaining(String nome, Pageable pageable) {
        // Implementação simplificada - buscar por nome exato
        return categoriaRepository.findByNome(nome)
                .map(categoria -> {
                    CategoriaResponse response = categoriaMapper.toResponse(categoria);
                    return new PageImpl<>(List.of(response), pageable, 1);
                })
                .orElse(new PageImpl<>(List.of(), pageable, 0));
    }

    public CategoriaResponse create(CategoriaRequest categoriaRequest) {
        // Verificar se já existe uma categoria com o mesmo nome
        if (categoriaRepository.existsByNome(categoriaRequest.getNome())) {
            throw new BadRequestException("Já existe uma categoria com o nome: " + categoriaRequest.getNome());
        }

        Categoria categoria = categoriaMapper.toEntity(categoriaRequest);
        Categoria categoriaSalva = categoriaRepository.save(categoria);
        return categoriaMapper.toResponse(categoriaSalva);
    }

    public CategoriaResponse update(Long id, CategoriaRequest categoriaRequest) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ApiConstants.ERRO_CATEGORIA_NAO_ENCONTRADA + id));

        // Verificar se o novo nome já está em uso por outra categoria
        if (!categoria.getNome().equalsIgnoreCase(categoriaRequest.getNome()) && 
            categoriaRepository.existsByNome(categoriaRequest.getNome())) {
            throw new BadRequestException("Já existe uma categoria com o nome: " + categoriaRequest.getNome());
        }

        categoriaMapper.updateEntityFromRequest(categoria, categoriaRequest);
        Categoria categoriaAtualizada = categoriaRepository.save(categoria);
        return categoriaMapper.toResponse(categoriaAtualizada);
    }

    public void delete(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ApiConstants.ERRO_CATEGORIA_NAO_ENCONTRADA + id));
        
        // Verificar se a categoria possui suplementos associados
        if (!categoria.getSuplementos().isEmpty()) {
            throw new BadRequestException("Não é possível excluir categoria com suplementos associados");
        }
        
        // Soft delete
        categoria.setActive(false);
        categoriaRepository.save(categoria);
    }

    @Transactional(readOnly = true)
    public boolean existsByNome(String nome) {
        return categoriaRepository.existsByNome(nome);
    }
}
