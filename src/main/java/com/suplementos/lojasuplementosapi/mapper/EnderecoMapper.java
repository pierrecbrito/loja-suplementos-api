package com.suplementos.lojasuplementosapi.mapper;

import com.suplementos.lojasuplementosapi.dto.EnderecoRequest;
import com.suplementos.lojasuplementosapi.dto.EnderecoResponse;
import com.suplementos.lojasuplementosapi.domain.Endereco;

import org.springframework.stereotype.Component;

@Component
public class EnderecoMapper {
    
    public Endereco toEntity(EnderecoRequest request) {
        if (request == null) {
            return null;
        }
        
        Endereco endereco = new Endereco();
        endereco.setRua(request.getRua());
        endereco.setNumero(request.getNumero());
        endereco.setComplemento(request.getComplemento());
        endereco.setBairro(request.getBairro());
        endereco.setCidade(request.getCidade());
        endereco.setEstado(request.getEstado());
        endereco.setCep(request.getCep());
        
        return endereco;
    }
    
    public void updateEntityFromRequest(Endereco endereco, EnderecoRequest request) {
        if (endereco == null || request == null) {
            return;
        }
        
        endereco.setRua(request.getRua());
        endereco.setNumero(request.getNumero());
        endereco.setComplemento(request.getComplemento());
        endereco.setBairro(request.getBairro());
        endereco.setCidade(request.getCidade());
        endereco.setEstado(request.getEstado());
        endereco.setCep(request.getCep());
    }
    
    public EnderecoResponse toResponse(Endereco endereco) {
        if (endereco == null) {
            return null;
        }
        
        return EnderecoResponse.builder()
                .id(endereco.getId())
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .complemento(endereco.getComplemento())
                .bairro(endereco.getBairro())
                .cidade(endereco.getCidade())
                .estado(endereco.getEstado())
                .cep(endereco.getCep())
                .build();
    }
}