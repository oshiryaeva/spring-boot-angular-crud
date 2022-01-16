package com.shiryaeva.wyrgorod.repository;

import com.shiryaeva.wyrgorod.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Modifying
    @Query("update Customer u set u.email = :newEmail where u.email = :oldEmail")
    void updateEmail(@Param(value = "oldEmail") String oldEmail, @Param(value = "newEmail") String newEmail);

    Customer findByEmailIgnoreCase(String email);

    void deleteByEmailIgnoreCase(String email);


}