package com.shiryaeva.wyrgorod.service;

import com.shiryaeva.wyrgorod.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public interface OrderService {

    BigDecimal calculateOrderAmount(Order order);

    Page<Order> findAll(Pageable pageable);

    Order findOne(Long id);

    Order save(Order order);

    Order update(Long id, Order request);

    void delete(Long id);

    List<Order> findAll();

    List<Order> findByCustomerId(Long id);

}
