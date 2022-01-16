package com.shiryaeva.wyrgorod.repository;

import com.shiryaeva.wyrgorod.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}