package com.suplementos.lojasuplementosapi.domain;

import com.suplementos.lojasuplementosapi.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario extends BaseEntity {

    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String senha;
    
    @Column(nullable = false)
    private String telefone;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    
    // Relacionamento 1-1
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Endereco endereco;
    
    // Relacionamento 1-N
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Set<Pedido> pedidos = new HashSet<>();
    
    // Relacionamento 1-N
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Set<Avaliacao> avaliacoes = new HashSet<>();
    
    // Enum para os papéis do usuário
    public enum Role {
        ADMIN, 
        CLIENTE
    }
}