package com.shiryaeva.wyrgorod.service.impl;

import com.shiryaeva.wyrgorod.model.OrderItem;
import com.shiryaeva.wyrgorod.repository.OrderItemRepository;
import com.shiryaeva.wyrgorod.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public class OrderItemServiceImpl implements OrderItemService {
    
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public Page<OrderItem> findAll(Pageable pageable) {
        return orderItemRepository.findAll(pageable);
    }

    @Override
    public OrderItem findOne(Long id) {
        return orderItemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public OrderItem save(OrderItem orderOrderItem) {
        return orderItemRepository.save(orderOrderItem);
    }

    @Override
    public OrderItem update(Long id, OrderItem request) {
        OrderItem existingOrderItem = orderItemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        existingOrderItem.setItem(request.getItem());
        existingOrderItem.setQuantity(request.getQuantity());
        existingOrderItem.setOrder(request.getOrder());
        return orderItemRepository.save(existingOrderItem);
    }

    @Override
    public void delete(Long id) {
        orderItemRepository.deleteById(id);
    }

    @Override
    public List<OrderItem> findAll() {
        return orderItemRepository.findAll();
    }


}
