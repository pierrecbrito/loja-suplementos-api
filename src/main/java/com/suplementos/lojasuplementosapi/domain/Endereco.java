package com.suplementos.lojasuplementosapi.domain;

import com.suplementos.lojasuplementosapi.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "enderecos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco extends BaseEntity {

    @Column(nullable = false)
    private String rua;
    
    @Column(nullable = false)
    private String numero;
    
    private String complemento;
    
    @Column(nullable = false)
    private String bairro;
    
    @Column(nullable = false)
    private String cidade;
    
    @Column(nullable = false)
    private String estado;
    
    @Column(nullable = false, name = "cep")
    private String cep;
    
    // Relacionamento 1-1 com Usuario
    @OneToOne(mappedBy = "endereco")
    private Usuario usuario;
}