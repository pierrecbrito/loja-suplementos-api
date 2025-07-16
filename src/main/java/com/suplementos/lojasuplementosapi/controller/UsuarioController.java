package com.suplementos.lojasuplementosapi.controller;

import com.suplementos.lojasuplementosapi.core.ApiConstants;
import com.suplementos.lojasuplementosapi.core.SecurityConstants;
import com.suplementos.lojasuplementosapi.dto.UsuarioRequest;
import com.suplementos.lojasuplementosapi.dto.UsuarioResponse;
import com.suplementos.lojasuplementosapi.service.UsuarioService;
import com.suplementos.lojasuplementosapi.hateoas.ResourceModel;
import com.suplementos.lojasuplementosapi.hateoas.PaginatedResourceModel;
import com.suplementos.lojasuplementosapi.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping(ApiConstants.USUARIOS_PATH)
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @PreAuthorize(SecurityConstants.HAS_ROLE_ADMIN)
    public ResponseEntity<PaginatedResourceModel<ResourceModel<UsuarioResponse>>> findAll(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        
        Page<UsuarioResponse> page = usuarioService.findAll(pageable);
        
        List<ResourceModel<UsuarioResponse>> resources = page.getContent().stream()
                .map(this::createResourceModel)
                .collect(Collectors.toList());
        
        PaginatedResourceModel<ResourceModel<UsuarioResponse>> resourceModel = 
                new PaginatedResourceModel<>(resources, page);
        
        // Adicionar links de paginação
        resourceModel.add(linkTo(methodOn(UsuarioController.class).findAll(pageable)).withSelfRel());
        
        if (page.hasNext()) {
            resourceModel.add(linkTo(methodOn(UsuarioController.class)
                    .findAll(pageable.next())).withRel("next"));
        }
        
        if (page.hasPrevious()) {
            resourceModel.add(linkTo(methodOn(UsuarioController.class)
                    .findAll(pageable.previousOrFirst())).withRel("previous"));
        }
        
        return ResponseEntity.ok(resourceModel);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @usuarioSecurity.isUsuario(#id)")
    public ResponseEntity<ResourceModel<UsuarioResponse>> findById(@PathVariable Long id) {
        UsuarioResponse usuario = usuarioService.findById(id);
        return ResponseEntity.ok(createResourceModel(usuario));
    }
    
    @GetMapping("/me")
    @PreAuthorize(SecurityConstants.IS_AUTHENTICATED)
    public ResponseEntity<ResourceModel<UsuarioResponse>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UsuarioResponse usuario = usuarioService.findById(userDetails.getId());
        return ResponseEntity.ok(createResourceModel(usuario));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @usuarioSecurity.isUsuario(#id)")
    public ResponseEntity<ResourceModel<UsuarioResponse>> update(
            @PathVariable Long id, 
            @Valid @RequestBody UsuarioRequest usuarioRequest) {
        
        UsuarioResponse usuario = usuarioService.update(id, usuarioRequest);
        return ResponseEntity.ok(createResourceModel(usuario));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @usuarioSecurity.isUsuario(#id)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    // Método auxiliar para criar o ResourceModel com links HATEOAS
    private ResourceModel<UsuarioResponse> createResourceModel(UsuarioResponse usuario) {
        ResourceModel<UsuarioResponse> resourceModel = new ResourceModel<>(usuario);
        
        // Links para o próprio recurso
        resourceModel.add(linkTo(methodOn(UsuarioController.class)
                .findById(usuario.getId())).withSelfRel());
        
        // Link para pedidos do usuário
        resourceModel.add(linkTo(methodOn(PedidoController.class)
                .findByUsuario(usuario.getId(), null)).withRel("pedidos"));
        
        // Link para avaliações do usuário
        resourceModel.add(linkTo(methodOn(AvaliacaoController.class)
                .findByUsuario(usuario.getId(), null)).withRel("avaliacoes"));
        
        return resourceModel;
    }
}