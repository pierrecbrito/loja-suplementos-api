package com.suplementos.lojasuplementosapi.domain;

import com.suplementos.lojasuplementosapi.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "avaliacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Avaliacao extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "suplemento_id", nullable = false)
    private Suplemento suplemento;
    
    @Column(nullable = false)
    private Integer nota;
    
    @Column(columnDefinition = "TEXT")
    private String comentario;
    
    @Column(name = "data_avaliacao", nullable = false)
    private LocalDateTime dataAvaliacao;
}