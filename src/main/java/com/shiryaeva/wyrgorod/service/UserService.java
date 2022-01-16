package com.shiryaeva.wyrgorod.service;

import com.shiryaeva.wyrgorod.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    Page<User> findAll(Pageable pageable);

    User findOne(Long id);

    User save(User artist);

    User update(Long id, User request);

    void delete(Long id);

    List<User> findAll();

}
