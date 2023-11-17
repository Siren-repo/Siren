package com.devlop.siren.domain.stock.repository;

import com.devlop.siren.domain.stock.entity.Stock;
import com.devlop.siren.domain.store.domain.Store;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("select s from Stock s join fetch s.store st join fetch s.item i where st.storeId = :storeId and i.itemId = :itemId ")
    Optional<Stock> findByStoreAndItem(@Param("storeId") Long storeId,
                                       @Param("itemId") Long itemId);

    Page<Stock> findAllByStore(Store store, Pageable pageable);
}
