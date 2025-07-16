package com.suplementos.lojasuplementosapi.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long> {
    
    List<T> findByActiveTrue();
    
    Optional<T> findByIdAndActiveTrue(Long id);
}