package com.shiryaeva.wyrgorod.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Table(name = "order_")
@Entity
@Getter
@Setter
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "amount", precision = 19, scale = 2)
    private BigDecimal amount;

    public void setAmount(BigDecimal amount) {
        this.amount = calculateOrderAmount();
    }

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addItem(OrderItem item) {
        this.orderItems.add(item);
        item.setOrder(this);
    }

    public void removeItem(OrderItem item) {
        this.orderItems.remove(item);
        item.setOrder(null);
    }


    private BigDecimal calculateOrderAmount() {
        BigDecimal amount = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            amount = amount.add(item.getItem().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return amount;
    }

}