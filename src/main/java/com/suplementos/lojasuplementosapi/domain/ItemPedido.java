package com.suplementos.lojasuplementosapi.domain;

import com.suplementos.lojasuplementosapi.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "itens_pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
    
    @ManyToOne
    @JoinColumn(name = "suplemento_id", nullable = false)
    private Suplemento suplemento;
    
    @Column(nullable = false)
    private Integer quantidade;
    
    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;
    
    // Método para calcular o subtotal do item
    public BigDecimal getSubtotal() {
        return precoUnitario.multiply(new BigDecimal(quantidade));
    }
}