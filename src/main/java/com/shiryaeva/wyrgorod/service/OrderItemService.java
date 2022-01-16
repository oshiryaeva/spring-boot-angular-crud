package com.shiryaeva.wyrgorod.service;

import com.shiryaeva.wyrgorod.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderItemService {

    Page<OrderItem> findAll(Pageable pageable);

    OrderItem findOne(Long id);

    OrderItem save(OrderItem artist);

    OrderItem update(Long id, OrderItem request);

    void delete(Long id);

    List<OrderItem> findAll();
}
