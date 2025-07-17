package com.suplementos.lojasuplementosapi.service;

import com.suplementos.lojasuplementosapi.core.ApiConstants;
import com.suplementos.lojasuplementosapi.domain.Usuario;
import com.suplementos.lojasuplementosapi.dto.UsuarioRequest;
import com.suplementos.lojasuplementosapi.dto.UsuarioUpdateRequest;
import com.suplementos.lojasuplementosapi.dto.UsuarioResponse;
import com.suplementos.lojasuplementosapi.erroHandling.BadRequestException;
import com.suplementos.lojasuplementosapi.erroHandling.ResourceNotFoundException;
import com.suplementos.lojasuplementosapi.mapper.UsuarioMapper;
import com.suplementos.lojasuplementosapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, 
                         UsuarioMapper usuarioMapper,
                         PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponse> findAll(Pageable pageable) {
        Page<Usuario> usuarios = usuarioRepository.findAll(pageable);
        return usuarios.map(usuarioMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ApiConstants.ERRO_USUARIO_NAO_ENCONTRADO + id));
        return usuarioMapper.toResponse(usuario);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public UsuarioResponse create(UsuarioRequest usuarioRequest) {
        // Verificar se o email já existe
        if (usuarioRepository.existsByEmail(usuarioRequest.getEmail())) {
            throw new BadRequestException("Email já está em uso: " + usuarioRequest.getEmail());
        }

        Usuario usuario = usuarioMapper.toEntity(usuarioRequest);
        
        // Criptografar senha
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        
        // Usar o role do request ou CLIENTE como padrão
        if (usuarioRequest.getRole() != null) {
            usuario.setRole(usuarioRequest.getRole());
        } else {
            usuario.setRole(Usuario.Role.CLIENTE);
        }

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuarioSalvo);
    }

    public UsuarioResponse update(Long id, UsuarioRequest usuarioRequest) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ApiConstants.ERRO_USUARIO_NAO_ENCONTRADO + id));

        // Verificar se o novo email já está em uso por outro usuário
        if (!usuario.getEmail().equals(usuarioRequest.getEmail()) && 
            usuarioRepository.existsByEmail(usuarioRequest.getEmail())) {
            throw new BadRequestException("Email já está em uso: " + usuarioRequest.getEmail());
        }

        usuarioMapper.updateEntityFromRequest(usuario, usuarioRequest);
        
        // Criptografar nova senha se fornecida
        if (usuarioRequest.getSenha() != null && !usuarioRequest.getSenha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(usuarioRequest.getSenha()));
        }

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuarioAtualizado);
    }

    public UsuarioResponse update(Long id, UsuarioUpdateRequest usuarioRequest) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ApiConstants.ERRO_USUARIO_NAO_ENCONTRADO + id));

        // Verificar se o novo email já está em uso por outro usuário (apenas se email foi fornecido)
        if (usuarioRequest.getEmail() != null && 
            !usuario.getEmail().equals(usuarioRequest.getEmail()) && 
            usuarioRepository.existsByEmail(usuarioRequest.getEmail())) {
            throw new BadRequestException("Email já está em uso: " + usuarioRequest.getEmail());
        }

        usuarioMapper.updateEntityFromUpdateRequest(usuario, usuarioRequest);
        
        // Criptografar nova senha se fornecida
        if (usuarioRequest.getSenha() != null && !usuarioRequest.getSenha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(usuarioRequest.getSenha()));
        }

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuarioAtualizado);
    }

    public void delete(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ApiConstants.ERRO_USUARIO_NAO_ENCONTRADO + id));
        
        // Soft delete
        usuario.setActive(false);
        usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
