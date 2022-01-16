package com.shiryaeva.wyrgorod.repository;

import com.shiryaeva.wyrgorod.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer_Id(Long id);


}