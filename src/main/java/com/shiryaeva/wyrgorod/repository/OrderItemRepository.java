package com.shiryaeva.wyrgorod.repository;

import com.shiryaeva.wyrgorod.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}