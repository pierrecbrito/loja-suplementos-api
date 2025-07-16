package com.suplementos.lojasuplementosapi.repository;

import com.suplementos.lojasuplementosapi.base.BaseRepository;
import com.suplementos.lojasuplementosapi.domain.Pedido;
import com.suplementos.lojasuplementosapi.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends BaseRepository<Pedido> {
    
    Page<Pedido> findByUsuarioAndActiveTrue(Usuario usuario, Pageable pageable);
    
    Page<Pedido> findByStatusAndActiveTrue(Pedido.StatusPedido status, Pageable pageable);
}