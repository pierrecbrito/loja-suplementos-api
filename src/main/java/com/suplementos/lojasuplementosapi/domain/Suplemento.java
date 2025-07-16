package com.suplementos.lojasuplementosapi.domain;

import com.suplementos.lojasuplementosapi.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "suplementos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Suplemento extends BaseEntity {

    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false)
    private String marca;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;
    
    @Column(name = "quantidade_estoque", nullable = false)
    private Integer quantidadeEstoque;
    
    @Column(name = "peso_gramas")
    private Integer pesoGramas;
    
    @Column(name = "imagem_url")
    private String imagemUrl;
    
    @Column(name = "codigo_barras", unique = true)
    private String codigoBarras;
    
    @Column(name = "data_validade")
    private LocalDate dataValidade;
    
    private boolean destaque = false;
    
    // Relacionamento N-N com Categoria
    @ManyToMany
    @JoinTable(
        name = "suplemento_categoria",
        joinColumns = @JoinColumn(name = "suplemento_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categorias = new HashSet<>();
    
    // Relacionamento 1-N com Avaliacao
    @OneToMany(mappedBy = "suplemento", cascade = CascadeType.ALL)
    private Set<Avaliacao> avaliacoes = new HashSet<>();
    
    // Relacionamento 1-N com ItemPedido
    @OneToMany(mappedBy = "suplemento")
    private Set<ItemPedido> itensPedido = new HashSet<>();
}