package com.suplementos.lojasuplementosapi.controller;

import com.suplementos.lojasuplementosapi.core.ApiConstants;
import com.suplementos.lojasuplementosapi.core.SecurityConstants;
import com.suplementos.lojasuplementosapi.dto.SuplementoRequest;
import com.suplementos.lojasuplementosapi.dto.SuplementoResponse;
import com.suplementos.lojasuplementosapi.hateoas.PaginatedResourceModel;
import com.suplementos.lojasuplementosapi.hateoas.ResourceModel;
import com.suplementos.lojasuplementosapi.service.SuplementoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(ApiConstants.SUPLEMENTOS_PATH)
public class SuplementoController {

    private final SuplementoService suplementoService;

    @Autowired
    public SuplementoController(SuplementoService suplementoService) {
        this.suplementoService = suplementoService;
    }

    @GetMapping
    public ResponseEntity<PaginatedResourceModel<ResourceModel<SuplementoResponse>>> findAll(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        
        Page<SuplementoResponse> page = suplementoService.findAll(pageable);
        
        List<ResourceModel<SuplementoResponse>> resources = page.getContent().stream()
                .map(this::createResourceModel)
                .collect(Collectors.toList());
        
        PaginatedResourceModel<ResourceModel<SuplementoResponse>> resourceModel = 
                new PaginatedResourceModel<>(resources, page);
        
        // Adicionar links de paginação
        resourceModel.add(linkTo(methodOn(SuplementoController.class).findAll(pageable)).withSelfRel());
        
        if (page.hasNext()) {
            resourceModel.add(linkTo(methodOn(SuplementoController.class)
                    .findAll(pageable.next())).withRel("next"));
        }
        
        if (page.hasPrevious()) {
            resourceModel.add(linkTo(methodOn(SuplementoController.class)
                    .findAll(pageable.previousOrFirst())).withRel("previous"));
        }
        
        return ResponseEntity.ok(resourceModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceModel<SuplementoResponse>> findById(@PathVariable Long id) {
        SuplementoResponse suplemento = suplementoService.findById(id);
        return ResponseEntity.ok(createResourceModel(suplemento));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<PaginatedResourceModel<ResourceModel<SuplementoResponse>>> findByCategoria(
            @PathVariable Long categoriaId,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        
        Page<SuplementoResponse> page = suplementoService.findByCategoria(categoriaId, pageable);
        
        List<ResourceModel<SuplementoResponse>> resources = page.getContent().stream()
                .map(this::createResourceModel)
                .collect(Collectors.toList());
        
        PaginatedResourceModel<ResourceModel<SuplementoResponse>> resourceModel = 
                new PaginatedResourceModel<>(resources, page);
        
        resourceModel.add(linkTo(methodOn(SuplementoController.class)
                .findByCategoria(categoriaId, pageable)).withSelfRel());
        
        return ResponseEntity.ok(resourceModel);
    }

    @GetMapping("/search")
    public ResponseEntity<PaginatedResourceModel<ResourceModel<SuplementoResponse>>> search(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String marca,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        
        Page<SuplementoResponse> page;
        
        if (nome != null && !nome.trim().isEmpty()) {
            page = suplementoService.findByNomeContaining(nome, pageable);
        } else if (marca != null && !marca.trim().isEmpty()) {
            page = suplementoService.findByMarca(marca, pageable);
        } else {
            page = suplementoService.findAll(pageable);
        }
        
        List<ResourceModel<SuplementoResponse>> resources = page.getContent().stream()
                .map(this::createResourceModel)
                .collect(Collectors.toList());
        
        PaginatedResourceModel<ResourceModel<SuplementoResponse>> resourceModel = 
                new PaginatedResourceModel<>(resources, page);
        
        resourceModel.add(linkTo(methodOn(SuplementoController.class)
                .search(nome, marca, pageable)).withSelfRel());
        
        return ResponseEntity.ok(resourceModel);
    }

    @PostMapping
    @PreAuthorize(SecurityConstants.HAS_ROLE_ADMIN)
    public ResponseEntity<ResourceModel<SuplementoResponse>> create(
            @Valid @RequestBody SuplementoRequest suplementoRequest) {
        
        SuplementoResponse suplemento = suplementoService.create(suplementoRequest);
        ResourceModel<SuplementoResponse> resourceModel = createResourceModel(suplemento);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceModel);
    }

    @PutMapping("/{id}")
    @PreAuthorize(SecurityConstants.HAS_ROLE_ADMIN)
    public ResponseEntity<ResourceModel<SuplementoResponse>> update(
            @PathVariable Long id, 
            @Valid @RequestBody SuplementoRequest suplementoRequest) {
        
        SuplementoResponse suplemento = suplementoService.update(id, suplementoRequest);
        return ResponseEntity.ok(createResourceModel(suplemento));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(SecurityConstants.HAS_ROLE_ADMIN)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        suplementoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Método auxiliar para criar o ResourceModel com links HATEOAS
    private ResourceModel<SuplementoResponse> createResourceModel(SuplementoResponse suplemento) {
        ResourceModel<SuplementoResponse> resourceModel = new ResourceModel<>(suplemento);
        
        // Links para o próprio recurso
        resourceModel.add(linkTo(methodOn(SuplementoController.class)
                .findById(suplemento.getId())).withSelfRel());
        
        // Link para avaliações do suplemento
        resourceModel.add(linkTo(methodOn(AvaliacaoController.class)
                .findBySuplemento(suplemento.getId(), null)).withRel("avaliacoes"));
        
        // Link para categoria (se existir)
        if (suplemento.getCategorias() != null && !suplemento.getCategorias().isEmpty()) {
            // Removido temporariamente até resolver dependência circular
            // resourceModel.add(linkTo(methodOn(CategoriaController.class)
            //         .findById(suplemento.getCategorias().iterator().next().getId())).withRel("categoria"));
        }
        
        return resourceModel;
    }
}
