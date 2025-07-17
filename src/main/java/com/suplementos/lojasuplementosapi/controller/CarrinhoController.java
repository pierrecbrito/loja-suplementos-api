package com.suplementos.lojasuplementosapi.controller;

import com.suplementos.lojasuplementosapi.core.ApiConstants;
import com.suplementos.lojasuplementosapi.core.SecurityConstants;
import com.suplementos.lojasuplementosapi.dto.CarrinhoResponse;
import com.suplementos.lojasuplementosapi.dto.ItemCarrinhoRequest;
import com.suplementos.lojasuplementosapi.hateoas.ResourceModel;
import com.suplementos.lojasuplementosapi.service.CarrinhoService;
import com.suplementos.lojasuplementosapi.service.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(ApiConstants.API_BASE_PATH + "/carrinho")
@RequiredArgsConstructor
public class CarrinhoController {
    
    private final CarrinhoService carrinhoService;
    
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_CLIENTE')")
    public ResponseEntity<ResourceModel<CarrinhoResponse>> obterCarrinho() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        CarrinhoResponse carrinho = carrinhoService.obterCarrinhoPorUsuario(userDetails.getId());
        ResourceModel<CarrinhoResponse> resourceModel = createResourceModel(carrinho);
        
        return ResponseEntity.ok(resourceModel);
    }
    
    @PostMapping("/itens")
    @PreAuthorize("hasAuthority('ROLE_CLIENTE')")
    public ResponseEntity<ResourceModel<CarrinhoResponse>> adicionarItem(
            @Valid @RequestBody ItemCarrinhoRequest request) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        CarrinhoResponse carrinho = carrinhoService.adicionarItem(userDetails.getId(), request);
        ResourceModel<CarrinhoResponse> resourceModel = createResourceModel(carrinho);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceModel);
    }
    
    @PutMapping("/itens/{itemId}")
    @PreAuthorize("hasAuthority('ROLE_CLIENTE')")
    public ResponseEntity<ResourceModel<CarrinhoResponse>> atualizarQuantidade(
            @PathVariable Long itemId,
            @RequestParam Integer quantidade) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        CarrinhoResponse carrinho = carrinhoService.atualizarQuantidadeItem(
                userDetails.getId(), itemId, quantidade);
        ResourceModel<CarrinhoResponse> resourceModel = createResourceModel(carrinho);
        
        return ResponseEntity.ok(resourceModel);
    }
    
    @DeleteMapping("/itens/{itemId}")
    @PreAuthorize("hasAuthority('ROLE_CLIENTE')")
    public ResponseEntity<ResourceModel<CarrinhoResponse>> removerItem(@PathVariable Long itemId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        CarrinhoResponse carrinho = carrinhoService.removerItem(userDetails.getId(), itemId);
        ResourceModel<CarrinhoResponse> resourceModel = createResourceModel(carrinho);
        
        return ResponseEntity.ok(resourceModel);
    }
    
    @DeleteMapping
    @PreAuthorize("hasAuthority('ROLE_CLIENTE')")
    public ResponseEntity<Void> limparCarrinho() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        carrinhoService.limparCarrinho(userDetails.getId());
        
        return ResponseEntity.noContent().build();
    }
    
    // Método auxiliar para criar ResourceModel com links HATEOAS
    private ResourceModel<CarrinhoResponse> createResourceModel(CarrinhoResponse carrinho) {
        ResourceModel<CarrinhoResponse> resourceModel = new ResourceModel<>(carrinho);
        
        // Link para o próprio carrinho
        resourceModel.add(linkTo(methodOn(CarrinhoController.class).obterCarrinho()).withSelfRel());
        
        // Link para adicionar item
        resourceModel.add(linkTo(methodOn(CarrinhoController.class)
                .adicionarItem(null)).withRel("adicionar-item"));
        
        // Link para limpar carrinho
        resourceModel.add(linkTo(methodOn(CarrinhoController.class)
                .limparCarrinho()).withRel("limpar"));
        
        return resourceModel;
    }
}
