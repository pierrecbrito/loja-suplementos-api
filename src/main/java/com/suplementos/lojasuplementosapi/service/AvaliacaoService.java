package com.suplementos.lojasuplementosapi.service;

import com.suplementos.lojasuplementosapi.core.ApiConstants;
import com.suplementos.lojasuplementosapi.domain.Avaliacao;
import com.suplementos.lojasuplementosapi.domain.Suplemento;
import com.suplementos.lojasuplementosapi.domain.Usuario;
import com.suplementos.lojasuplementosapi.dto.AvaliacaoRequest;
import com.suplementos.lojasuplementosapi.dto.AvaliacaoResponse;
import com.suplementos.lojasuplementosapi.erroHandling.BadRequestException;
import com.suplementos.lojasuplementosapi.erroHandling.ResourceNotFoundException;
import com.suplementos.lojasuplementosapi.mapper.AvaliacaoMapper;
import com.suplementos.lojasuplementosapi.repository.AvaliacaoRepository;
import com.suplementos.lojasuplementosapi.repository.SuplementoRepository;
import com.suplementos.lojasuplementosapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SuplementoRepository suplementoRepository;
    private final AvaliacaoMapper avaliacaoMapper;

    @Autowired
    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository,
                           UsuarioRepository usuarioRepository,
                           SuplementoRepository suplementoRepository,
                           AvaliacaoMapper avaliacaoMapper) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.suplementoRepository = suplementoRepository;
        this.avaliacaoMapper = avaliacaoMapper;
    }

    @Transactional(readOnly = true)
    public Page<AvaliacaoResponse> findAll(Pageable pageable) {
        Page<Avaliacao> avaliacoes = avaliacaoRepository.findAll(pageable);
        return avaliacoes.map(avaliacaoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public AvaliacaoResponse findById(Long id) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ApiConstants.ERRO_AVALIACAO_NAO_ENCONTRADA + id));
        return avaliacaoMapper.toResponse(avaliacao);
    }

    @Transactional(readOnly = true)
    public Page<AvaliacaoResponse> findBySuplemento(Long suplementoId, Pageable pageable) {
        Suplemento suplemento = suplementoRepository.findById(suplementoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ApiConstants.ERRO_SUPLEMENTO_NAO_ENCONTRADO + suplementoId));
        
        Page<Avaliacao> avaliacoes = avaliacaoRepository.findBySuplementoAndActiveTrue(suplemento, pageable);
        return avaliacoes.map(avaliacaoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<AvaliacaoResponse> findByUsuario(Long usuarioId, Pageable pageable) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ApiConstants.ERRO_USUARIO_NAO_ENCONTRADO + usuarioId));
        
        Page<Avaliacao> avaliacoes = avaliacaoRepository.findByUsuarioAndActiveTrue(usuario, pageable);
        return avaliacoes.map(avaliacaoMapper::toResponse);
    }

    public AvaliacaoResponse create(AvaliacaoRequest avaliacaoRequest, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ApiConstants.ERRO_USUARIO_NAO_ENCONTRADO + usuarioId));

        Suplemento suplemento = suplementoRepository.findById(avaliacaoRequest.getSuplementoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        ApiConstants.ERRO_SUPLEMENTO_NAO_ENCONTRADO + avaliacaoRequest.getSuplementoId()));

        // Verificar se o usuário já avaliou este suplemento
        if (avaliacaoRepository.findBySuplementoAndUsuarioAndActiveTrue(suplemento, usuario).isPresent()) {
            throw new BadRequestException("Usuário já avaliou este suplemento");
        }

        Avaliacao avaliacao = avaliacaoMapper.toEntity(avaliacaoRequest);
        avaliacao.setUsuario(usuario);
        avaliacao.setSuplemento(suplemento);
        avaliacao.setDataAvaliacao(LocalDateTime.now());

        Avaliacao avaliacaoSalva = avaliacaoRepository.save(avaliacao);
        return avaliacaoMapper.toResponse(avaliacaoSalva);
    }

    public AvaliacaoResponse update(Long id, AvaliacaoRequest avaliacaoRequest, Long usuarioId) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ApiConstants.ERRO_AVALIACAO_NAO_ENCONTRADA + id));

        // Verificar se o usuário é o dono da avaliação
        if (!avaliacao.getUsuario().getId().equals(usuarioId)) {
            throw new BadRequestException("Usuário não pode alterar avaliação de outro usuário");
        }

        avaliacaoMapper.updateEntityFromRequest(avaliacao, avaliacaoRequest);
        Avaliacao avaliacaoAtualizada = avaliacaoRepository.save(avaliacao);
        return avaliacaoMapper.toResponse(avaliacaoAtualizada);
    }

    public void delete(Long id, Long usuarioId) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ApiConstants.ERRO_AVALIACAO_NAO_ENCONTRADA + id));
        
        // Verificar se o usuário é o dono da avaliação
        if (!avaliacao.getUsuario().getId().equals(usuarioId)) {
            throw new BadRequestException("Usuário não pode deletar avaliação de outro usuário");
        }
        
        // Soft delete
        avaliacao.setActive(false);
        avaliacaoRepository.save(avaliacao);
    }
}
