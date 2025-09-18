package com.mehmetsadullahguven.repository;

import com.mehmetsadullahguven.model.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findFirstByR2sProductListIdOrderByCreatedAtDesc(String r2sProductListId);

    List<Order> findByStatusAndR2sProductListId(String status, String r2sProductListId);

    Optional<Order> findByClientOrderIdAndR2sProductListId(String clientOrderId, String r2sProductListId);
}
