package com.suplementos.lojasuplementosapi.controller;

import com.suplementos.lojasuplementosapi.core.ApiConstants;
import com.suplementos.lojasuplementosapi.core.SecurityConstants;
import com.suplementos.lojasuplementosapi.domain.Pedido;
import com.suplementos.lojasuplementosapi.dto.PedidoRequest;
import com.suplementos.lojasuplementosapi.dto.PedidoResponse;
import com.suplementos.lojasuplementosapi.hateoas.PaginatedResourceModel;
import com.suplementos.lojasuplementosapi.hateoas.ResourceModel;
import com.suplementos.lojasuplementosapi.service.PedidoService;
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
@RequestMapping(ApiConstants.PEDIDOS_PATH)
public class PedidoController {

    private final PedidoService pedidoService;

    @Autowired
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    @PreAuthorize(SecurityConstants.HAS_ROLE_ADMIN)
    public ResponseEntity<PaginatedResourceModel<ResourceModel<PedidoResponse>>> findAll(
            @PageableDefault(size = 10, sort = "dataPedido") Pageable pageable) {
        
        Page<PedidoResponse> page = pedidoService.findAll(pageable);
        
        List<ResourceModel<PedidoResponse>> resources = page.getContent().stream()
                .map(this::createResourceModel)
                .collect(Collectors.toList());
        
        PaginatedResourceModel<ResourceModel<PedidoResponse>> resourceModel = 
                new PaginatedResourceModel<>(resources, page);
        
        resourceModel.add(linkTo(methodOn(PedidoController.class).findAll(pageable)).withSelfRel());
        
        return ResponseEntity.ok(resourceModel);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @pedidoSecurity.isOwner(#id)")
    public ResponseEntity<ResourceModel<PedidoResponse>> findById(@PathVariable Long id) {
        PedidoResponse pedido = pedidoService.findById(id);
        return ResponseEntity.ok(createResourceModel(pedido));
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @usuarioSecurity.isUsuario(#usuarioId)")
    public ResponseEntity<PaginatedResourceModel<ResourceModel<PedidoResponse>>> findByUsuario(
            @PathVariable Long usuarioId,
            @PageableDefault(size = 10, sort = "dataPedido") Pageable pageable) {
        
        Page<PedidoResponse> page = pedidoService.findByUsuario(usuarioId, pageable);
        
        List<ResourceModel<PedidoResponse>> resources = page.getContent().stream()
                .map(this::createResourceModel)
                .collect(Collectors.toList());
        
        PaginatedResourceModel<ResourceModel<PedidoResponse>> resourceModel = 
                new PaginatedResourceModel<>(resources, page);
        
        resourceModel.add(linkTo(methodOn(PedidoController.class)
                .findByUsuario(usuarioId, pageable)).withSelfRel());
        
        return ResponseEntity.ok(resourceModel);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize(SecurityConstants.HAS_ROLE_ADMIN)
    public ResponseEntity<PaginatedResourceModel<ResourceModel<PedidoResponse>>> findByStatus(
            @PathVariable Pedido.StatusPedido status,
            @PageableDefault(size = 10, sort = "dataPedido") Pageable pageable) {
        
        Page<PedidoResponse> page = pedidoService.findByStatus(status, pageable);
        
        List<ResourceModel<PedidoResponse>> resources = page.getContent().stream()
                .map(this::createResourceModel)
                .collect(Collectors.toList());
        
        PaginatedResourceModel<ResourceModel<PedidoResponse>> resourceModel = 
                new PaginatedResourceModel<>(resources, page);
        
        resourceModel.add(linkTo(methodOn(PedidoController.class)
                .findByStatus(status, pageable)).withSelfRel());
        
        return ResponseEntity.ok(resourceModel);
    }

    @PostMapping
    @PreAuthorize(SecurityConstants.IS_AUTHENTICATED)
    public ResponseEntity<ResourceModel<PedidoResponse>> create(
            @Valid @RequestBody PedidoRequest pedidoRequest) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        PedidoResponse pedido = pedidoService.create(pedidoRequest, userDetails.getId());
        ResourceModel<PedidoResponse> resourceModel = createResourceModel(pedido);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceModel);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize(SecurityConstants.HAS_ROLE_ADMIN)
    public ResponseEntity<ResourceModel<PedidoResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam Pedido.StatusPedido status) {
        
        PedidoResponse pedido = pedidoService.updateStatus(id, status);
        return ResponseEntity.ok(createResourceModel(pedido));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @pedidoSecurity.isOwner(#id)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pedidoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Método auxiliar para criar o ResourceModel com links HATEOAS
    private ResourceModel<PedidoResponse> createResourceModel(PedidoResponse pedido) {
        ResourceModel<PedidoResponse> resourceModel = new ResourceModel<>(pedido);
        
        // Links para o próprio recurso
        resourceModel.add(linkTo(methodOn(PedidoController.class)
                .findById(pedido.getId())).withSelfRel());
        
        // Link para o usuário
        resourceModel.add(linkTo(methodOn(UsuarioController.class)
                .findById(pedido.getUsuarioId())).withRel("usuario"));
        
        // Link para atualizar status (se admin)
        resourceModel.add(linkTo(methodOn(PedidoController.class)
                .updateStatus(pedido.getId(), null)).withRel("update-status"));
        
        return resourceModel;
    }
}
