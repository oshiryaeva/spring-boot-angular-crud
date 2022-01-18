package com.shiryaeva.wyrgorod.service.impl;

import com.shiryaeva.wyrgorod.model.Customer;
import com.shiryaeva.wyrgorod.repository.CustomerRepository;
import com.shiryaeva.wyrgorod.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Override
    public Customer findOne(Long id) {
        return customerRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(Long id, Customer request) {
        Customer existingCustomer = customerRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        existingCustomer.setFirstName(request.getFirstName());
        existingCustomer.setLastName(request.getLastName());
        existingCustomer.setEmail(request.getEmail());
        return customerRepository.save(existingCustomer);
    }

    @Override
    public void delete(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public void deleteByEmail(String email) {
        customerRepository.deleteByEmailIgnoreCase(email);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer updateEmail(String oldEmail, String newEmail) {
        customerRepository.updateEmail(oldEmail, newEmail);
        return customerRepository.findByEmailIgnoreCase(newEmail);
    }
}
