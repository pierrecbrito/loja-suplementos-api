package com.suplementos.lojasuplementosapi.domain;

import com.suplementos.lojasuplementosapi.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "itens_carrinho")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarrinho extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "carrinho_id", nullable = false)
    private Carrinho carrinho;
    
    @ManyToOne
    @JoinColumn(name = "suplemento_id", nullable = false)
    private Suplemento suplemento;
    
    @Column(nullable = false)
    private Integer quantidade;
    
    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;
    
    // Método para calcular o subtotal do item
    public BigDecimal calcularSubtotal() {
        return precoUnitario.multiply(new BigDecimal(quantidade));
    }
}
