package com.devlop.siren.domain.store.repository;

import com.devlop.siren.domain.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store,Long> {
    Optional<Store> findByStoreId(Long storeId);
    Long deleteByStoreId(Long storeId);
}