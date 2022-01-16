package com.shiryaeva.wyrgorod.service.impl;

import com.shiryaeva.wyrgorod.model.Order;
import com.shiryaeva.wyrgorod.model.OrderItem;
import com.shiryaeva.wyrgorod.repository.OrderRepository;
import com.shiryaeva.wyrgorod.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public BigDecimal calculateOrderAmount(Order order) {
        BigDecimal amount = BigDecimal.ZERO;
        for (OrderItem item : order.getOrderItems()) {
            amount = amount.add(item.getItem().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return amount;
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public Order findOne(Long id) {
        return orderRepository.findById(id).orElseThrow(EntityNotFoundException
                ::new);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order update(Long id, Order request) {
        Order existingOrder = orderRepository.getById(id);
        existingOrder.setCustomer(request.getCustomer());
        existingOrder.setOrderItems(request.getOrderItems());
        existingOrder.setAmount(request.getAmount());
        return orderRepository.save(existingOrder);
    }

    @Override
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> findByCustomerId(Long id) {
        return orderRepository.findByCustomer_Id(id);
    }
}
