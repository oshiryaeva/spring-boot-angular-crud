package com.shiryaeva.wyrgorod.service;

import com.shiryaeva.wyrgorod.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {
    Page<Customer> findAll(Pageable pageable);

    Customer findOne(Long id);

    Customer findByEmail(String email);

    Customer save(Customer customer);

    Customer update(Long id, Customer request);

    void delete(Long id);

    void deleteByEmail(String email);

    List<Customer> findAll();

    Customer updateEmail(String oldEmail, String newEmail);
}
