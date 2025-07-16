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
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedido extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @Column(name = "data_pedido", nullable = false)
    private LocalDateTime dataPedido;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status;
    
    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;
    
    @Column(name = "forma_pagamento", nullable = false)
    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemPedido> itens = new HashSet<>();
    
    // Enums para status do pedido e forma de pagamento
    public enum StatusPedido {
        PENDENTE,
        PAGO,
        ENVIADO,
        ENTREGUE,
        CANCELADO
    }
    
    public enum FormaPagamento {
        CARTAO_CREDITO,
        CARTAO_DEBITO,
        PIX,
        BOLETO,
        TRANSFERENCIA
    }
    
    // Método para calcular o valor total do pedido
    public void calcularValorTotal() {
        this.valorTotal = this.itens.stream()
                .map(item -> item.getPrecoUnitario().multiply(new BigDecimal(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}