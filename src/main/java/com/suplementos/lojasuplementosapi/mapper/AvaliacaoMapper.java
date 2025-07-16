package com.suplementos.lojasuplementosapi.mapper;

import com.suplementos.lojasuplementosapi.domain.Avaliacao;
import com.suplementos.lojasuplementosapi.dto.AvaliacaoRequest;
import com.suplementos.lojasuplementosapi.dto.AvaliacaoResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AvaliacaoMapper {
    
    public Avaliacao toEntity(AvaliacaoRequest request) {
        if (request == null) {
            return null;
        }
        
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setNota(request.getNota());
        avaliacao.setComentario(request.getComentario());
        
        return avaliacao;
    }
    
    public void updateEntityFromRequest(Avaliacao avaliacao, AvaliacaoRequest request) {
        if (request == null) {
            return;
        }
        
        avaliacao.setNota(request.getNota());
        avaliacao.setComentario(request.getComentario());
    }
    
    public AvaliacaoResponse toResponse(Avaliacao avaliacao) {
        if (avaliacao == null) {
            return null;
        }
        
        return AvaliacaoResponse.builder()
                .id(avaliacao.getId())
                .nota(avaliacao.getNota())
                .comentario(avaliacao.getComentario())
                .dataAvaliacao(avaliacao.getDataAvaliacao())
                .usuarioId(avaliacao.getUsuario().getId())
                .nomeUsuario(avaliacao.getUsuario().getNome())
                .suplementoId(avaliacao.getSuplemento().getId())
                .nomeSuplemento(avaliacao.getSuplemento().getNome())
                .build();
    }
    
    public List<AvaliacaoResponse> toResponseList(List<Avaliacao> avaliacoes) {
        return avaliacoes.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
