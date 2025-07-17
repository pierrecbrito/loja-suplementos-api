package com.suplementos.lojasuplementosapi.controller;

import com.suplementos.lojasuplementosapi.core.ApiConstants;
import com.suplementos.lojasuplementosapi.core.SecurityConstants;
import com.suplementos.lojasuplementosapi.dto.CategoriaRequest;
import com.suplementos.lojasuplementosapi.dto.CategoriaResponse;
import com.suplementos.lojasuplementosapi.hateoas.PaginatedResourceModel;
import com.suplementos.lojasuplementosapi.hateoas.ResourceModel;
import com.suplementos.lojasuplementosapi.service.CategoriaService;
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
@RequestMapping(ApiConstants.CATEGORIAS_PATH)
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<PaginatedResourceModel<ResourceModel<CategoriaResponse>>> findAll(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        
        Page<CategoriaResponse> page = categoriaService.findAll(pageable);
        
        List<ResourceModel<CategoriaResponse>> resources = page.getContent().stream()
                .map(this::createResourceModel)
                .collect(Collectors.toList());
        
        PaginatedResourceModel<ResourceModel<CategoriaResponse>> resourceModel = 
                new PaginatedResourceModel<>(resources, page);
        
        // Adicionar links de paginação
        resourceModel.add(linkTo(methodOn(CategoriaController.class).findAll(pageable)).withSelfRel());
        
        if (page.hasNext()) {
            resourceModel.add(linkTo(methodOn(CategoriaController.class)
                    .findAll(pageable.next())).withRel("next"));
        }
        
        if (page.hasPrevious()) {
            resourceModel.add(linkTo(methodOn(CategoriaController.class)
                    .findAll(pageable.previousOrFirst())).withRel("previous"));
        }
        
        return ResponseEntity.ok(resourceModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceModel<CategoriaResponse>> findById(@PathVariable Long id) {
        CategoriaResponse categoria = categoriaService.findById(id);
        return ResponseEntity.ok(createResourceModel(categoria));
    }

    @PostMapping
    @PreAuthorize(SecurityConstants.HAS_ROLE_ADMIN)
    public ResponseEntity<ResourceModel<CategoriaResponse>> create(@Valid @RequestBody CategoriaRequest categoriaRequest) {
        CategoriaResponse categoria = categoriaService.create(categoriaRequest);
        ResourceModel<CategoriaResponse> resourceModel = createResourceModel(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceModel);
    }

    @PutMapping("/{id}")
    @PreAuthorize(SecurityConstants.HAS_ROLE_ADMIN)
    public ResponseEntity<ResourceModel<CategoriaResponse>> update(
            @PathVariable Long id, 
            @Valid @RequestBody CategoriaRequest categoriaRequest) {
        
        CategoriaResponse categoria = categoriaService.update(id, categoriaRequest);
        return ResponseEntity.ok(createResourceModel(categoria));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(SecurityConstants.HAS_ROLE_ADMIN)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    // Método auxiliar para criar o ResourceModel com links HATEOAS
    private ResourceModel<CategoriaResponse> createResourceModel(CategoriaResponse categoria) {
        ResourceModel<CategoriaResponse> resourceModel = new ResourceModel<>(categoria);
        
        // Links para o próprio recurso
        resourceModel.add(linkTo(methodOn(CategoriaController.class)
                .findById(categoria.getId())).withSelfRel());
        
        return resourceModel;
    }
}
