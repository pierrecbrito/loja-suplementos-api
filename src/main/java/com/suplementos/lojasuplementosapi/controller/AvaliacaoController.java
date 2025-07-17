package com.suplementos.lojasuplementosapi.controller;

import com.suplementos.lojasuplementosapi.core.ApiConstants;
import com.suplementos.lojasuplementosapi.core.SecurityConstants;
import com.suplementos.lojasuplementosapi.dto.AvaliacaoRequest;
import com.suplementos.lojasuplementosapi.dto.AvaliacaoResponse;
import com.suplementos.lojasuplementosapi.hateoas.PaginatedResourceModel;
import com.suplementos.lojasuplementosapi.hateoas.ResourceModel;
import com.suplementos.lojasuplementosapi.service.AvaliacaoService;
import com.suplementos.lojasuplementosapi.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(ApiConstants.AVALIACOES_PATH)
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    @Autowired
    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    @GetMapping
    @PreAuthorize(SecurityConstants.IS_AUTHENTICATED)
    public ResponseEntity<PaginatedResourceModel<ResourceModel<AvaliacaoResponse>>> findAll(
            @PageableDefault(size = 10, sort = "dataAvaliacao") Pageable pageable) {
        
        Page<AvaliacaoResponse> page = avaliacaoService.findAll(pageable);
        
        List<ResourceModel<AvaliacaoResponse>> resources = page.getContent().stream()
                .map(this::createResourceModel)
                .collect(Collectors.toList());
        
        PaginatedResourceModel<ResourceModel<AvaliacaoResponse>> resourceModel = 
                new PaginatedResourceModel<>(resources, page);
        
        resourceModel.add(linkTo(methodOn(AvaliacaoController.class).findAll(pageable)).withSelfRel());
        
        return ResponseEntity.ok(resourceModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceModel<AvaliacaoResponse>> findById(@PathVariable Long id) {
        AvaliacaoResponse avaliacao = avaliacaoService.findById(id);
        return ResponseEntity.ok(createResourceModel(avaliacao));
    }

    @GetMapping("/suplemento/{suplementoId}")
    public ResponseEntity<PaginatedResourceModel<ResourceModel<AvaliacaoResponse>>> findBySuplemento(
            @PathVariable Long suplementoId,
            @PageableDefault(size = 10, sort = "dataAvaliacao") Pageable pageable) {
        
        Page<AvaliacaoResponse> page = avaliacaoService.findBySuplemento(suplementoId, pageable);
        
        List<ResourceModel<AvaliacaoResponse>> resources = page.getContent().stream()
                .map(this::createResourceModel)
                .collect(Collectors.toList());
        
        PaginatedResourceModel<ResourceModel<AvaliacaoResponse>> resourceModel = 
                new PaginatedResourceModel<>(resources, page);
        
        resourceModel.add(linkTo(methodOn(AvaliacaoController.class)
                .findBySuplemento(suplementoId, pageable)).withSelfRel());
        
        return ResponseEntity.ok(resourceModel);
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasAuthority('ADMIN') or @usuarioSecurity.isUsuario(#usuarioId)")
    public ResponseEntity<PaginatedResourceModel<ResourceModel<AvaliacaoResponse>>> findByUsuario(
            @PathVariable Long usuarioId,
            @PageableDefault(size = 10, sort = "dataAvaliacao") Pageable pageable) {
        
        Page<AvaliacaoResponse> page = avaliacaoService.findByUsuario(usuarioId, pageable);
        
        List<ResourceModel<AvaliacaoResponse>> resources = page.getContent().stream()
                .map(this::createResourceModel)
                .collect(Collectors.toList());
        
        PaginatedResourceModel<ResourceModel<AvaliacaoResponse>> resourceModel = 
                new PaginatedResourceModel<>(resources, page);
        
        resourceModel.add(linkTo(methodOn(AvaliacaoController.class)
                .findByUsuario(usuarioId, pageable)).withSelfRel());
        
        return ResponseEntity.ok(resourceModel);
    }

    @PostMapping
    @PreAuthorize(SecurityConstants.IS_AUTHENTICATED)
    public ResponseEntity<ResourceModel<AvaliacaoResponse>> create(
            @Valid @RequestBody AvaliacaoRequest avaliacaoRequest) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        AvaliacaoResponse avaliacao = avaliacaoService.create(avaliacaoRequest, userDetails.getId());
        ResourceModel<AvaliacaoResponse> resourceModel = createResourceModel(avaliacao);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceModel);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @avaliacaoSecurity.isOwner(#id)")
    public ResponseEntity<ResourceModel<AvaliacaoResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody AvaliacaoRequest avaliacaoRequest) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        AvaliacaoResponse avaliacao = avaliacaoService.update(id, avaliacaoRequest, userDetails.getId());
        return ResponseEntity.ok(createResourceModel(avaliacao));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @avaliacaoSecurity.isOwner(#id)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        avaliacaoService.delete(id, userDetails.getId());
        return ResponseEntity.noContent().build();
    }

    // Método auxiliar para criar o ResourceModel com links HATEOAS
    private ResourceModel<AvaliacaoResponse> createResourceModel(AvaliacaoResponse avaliacao) {
        ResourceModel<AvaliacaoResponse> resourceModel = new ResourceModel<>(avaliacao);
        
        // Links para o próprio recurso
        resourceModel.add(linkTo(methodOn(AvaliacaoController.class)
                .findById(avaliacao.getId())).withSelfRel());
        
        // Link para o usuário
        resourceModel.add(linkTo(methodOn(UsuarioController.class)
                .findById(avaliacao.getUsuarioId())).withRel("usuario"));
        
        // Link para o suplemento
        resourceModel.add(linkTo(methodOn(SuplementoController.class)
                .findById(avaliacao.getSuplementoId())).withRel("suplemento"));
        
        return resourceModel;
    }
}
