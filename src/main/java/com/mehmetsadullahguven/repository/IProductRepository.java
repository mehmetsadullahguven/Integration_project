package com.mehmetsadullahguven.repository;

import com.mehmetsadullahguven.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {

    @Query("FROM Product r WHERE r.r2sProductId = :r2sProductId AND r.r2sProductListId = :r2sProductListId")
    Optional<Product> findByR2sProductIdAndR2sProductListId(@Param("r2sProductId") String r2sProductId, @Param("r2sProductListId") String r2sProductListId);

    Page<Product> findByStatusAndR2sProductListId(String status,String r2sProductId , Pageable pageable);

    List<Product> findByR2sProductIdAndCorrelationId(String r2sProductId, String correlationId);

}
