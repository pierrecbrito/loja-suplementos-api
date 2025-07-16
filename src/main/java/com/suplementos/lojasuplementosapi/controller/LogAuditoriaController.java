package com.suplementos.lojasuplementosapi.controller;

import com.suplementos.lojasuplementosapi.core.ApiConstants;
import com.suplementos.lojasuplementosapi.core.SecurityConstants;
import com.suplementos.lojasuplementosapi.domain.LogAuditoria;
import com.suplementos.lojasuplementosapi.dto.LogAuditoriaResponse;
import com.suplementos.lojasuplementosapi.hateoas.PaginatedResourceModel;
import com.suplementos.lojasuplementosapi.hateoas.ResourceModel;
import com.suplementos.lojasuplementosapi.mapper.LogAuditoriaMapper;
import com.suplementos.lojasuplementosapi.repository.LogAuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(ApiConstants.API_BASE_PATH + "/logs")
@RequiredArgsConstructor
public class LogAuditoriaController {
    
    private final LogAuditoriaRepository logAuditoriaRepository;
    private final LogAuditoriaMapper logAuditoriaMapper;
    
    @GetMapping
    @PreAuthorize(SecurityConstants.HAS_ROLE_ADMIN)
    public ResponseEntity<PaginatedResourceModel<ResourceModel<LogAuditoriaResponse>>> findAll(
            @PageableDefault(size = 20, sort = "timestamp") Pageable pageable) {
        
        Page<LogAuditoria> page = logAuditoriaRepository.findRecentes(pageable);
        
        List<ResourceModel<LogAuditoriaResponse>> resources = page.getContent().stream()
                .map(log -> {
                    LogAuditoriaResponse response = logAuditoriaMapper.toResponse(log);
                    ResourceModel<LogAuditoriaResponse> resourceModel = new ResourceModel<>(response);
                    resourceModel.add(linkTo(methodOn(LogAuditoriaController.class)
                            .findById(log.getId())).withSelfRel());
                    return resourceModel;
                })
                .collect(Collectors.toList());
        
        PaginatedResourceModel<ResourceModel<LogAuditoriaResponse>> resourceModel = 
                new PaginatedResourceModel<>(resources, page);
        
        resourceModel.add(linkTo(methodOn(LogAuditoriaController.class).findAll(pageable)).withSelfRel());
        
        return ResponseEntity.ok(resourceModel);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize(SecurityConstants.HAS_ROLE_ADMIN)
    public ResponseEntity<ResourceModel<LogAuditoriaResponse>> findById(@PathVariable Long id) {
        LogAuditoria log = logAuditoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Log não encontrado"));
        
        LogAuditoriaResponse response = logAuditoriaMapper.toResponse(log);
        ResourceModel<LogAuditoriaResponse> resourceModel = new ResourceModel<>(response);
        resourceModel.add(linkTo(methodOn(LogAuditoriaController.class).findById(id)).withSelfRel());
        
        return ResponseEntity.ok(resourceModel);
    }
    
    @GetMapping("/tabela/{tabela}")
    @PreAuthorize(SecurityConstants.HAS_ROLE_ADMIN)
    public ResponseEntity<PaginatedResourceModel<ResourceModel<LogAuditoriaResponse>>> findByTabela(
            @PathVariable String tabela,
            @PageableDefault(size = 20, sort = "timestamp") Pageable pageable) {
        
        Page<LogAuditoria> page = logAuditoriaRepository.findByTabela(tabela, pageable);
        
        List<ResourceModel<LogAuditoriaResponse>> resources = page.getContent().stream()
                .map(log -> {
                    LogAuditoriaResponse response = logAuditoriaMapper.toResponse(log);
                    ResourceModel<LogAuditoriaResponse> resourceModel = new ResourceModel<>(response);
                    resourceModel.add(linkTo(methodOn(LogAuditoriaController.class)
                            .findById(log.getId())).withSelfRel());
                    return resourceModel;
                })
                .collect(Collectors.toList());
        
        PaginatedResourceModel<ResourceModel<LogAuditoriaResponse>> resourceModel = 
                new PaginatedResourceModel<>(resources, page);
        
        resourceModel.add(linkTo(methodOn(LogAuditoriaController.class)
                .findByTabela(tabela, pageable)).withSelfRel());
        
        return ResponseEntity.ok(resourceModel);
    }
    
    @GetMapping("/operacao/{operacao}")
    @PreAuthorize(SecurityConstants.HAS_ROLE_ADMIN)
    public ResponseEntity<PaginatedResourceModel<ResourceModel<LogAuditoriaResponse>>> findByOperacao(
            @PathVariable LogAuditoria.TipoOperacao operacao,
            @PageableDefault(size = 20, sort = "timestamp") Pageable pageable) {
        
        Page<LogAuditoria> page = logAuditoriaRepository.findByOperacao(operacao, pageable);
        
        List<ResourceModel<LogAuditoriaResponse>> resources = page.getContent().stream()
                .map(log -> {
                    LogAuditoriaResponse response = logAuditoriaMapper.toResponse(log);
                    ResourceModel<LogAuditoriaResponse> resourceModel = new ResourceModel<>(response);
                    resourceModel.add(linkTo(methodOn(LogAuditoriaController.class)
                            .findById(log.getId())).withSelfRel());
                    return resourceModel;
                })
                .collect(Collectors.toList());
        
        PaginatedResourceModel<ResourceModel<LogAuditoriaResponse>> resourceModel = 
                new PaginatedResourceModel<>(resources, page);
        
        resourceModel.add(linkTo(methodOn(LogAuditoriaController.class)
                .findByOperacao(operacao, pageable)).withSelfRel());
        
        return ResponseEntity.ok(resourceModel);
    }
}
