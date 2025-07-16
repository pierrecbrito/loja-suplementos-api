package com.suplementos.lojasuplementosapi.domain;

import com.suplementos.lojasuplementosapi.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "carrinhos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Carrinho extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
    
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal valorTotal = BigDecimal.ZERO;
    
    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemCarrinho> itens = new HashSet<>();
    
    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
    
    // Método para calcular o valor total do carrinho
    public void calcularValorTotal() {
        this.valorTotal = this.itens.stream()
                .map(item -> item.getPrecoUnitario().multiply(new BigDecimal(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    // Método para adicionar item ao carrinho
    public void adicionarItem(ItemCarrinho item) {
        item.setCarrinho(this);
        this.itens.add(item);
        calcularValorTotal();
    }
    
    // Método para remover item do carrinho
    public void removerItem(ItemCarrinho item) {
        this.itens.remove(item);
        calcularValorTotal();
    }
    
    // Método para limpar carrinho
    public void limpar() {
        this.itens.clear();
        this.valorTotal = BigDecimal.ZERO;
    }
}
