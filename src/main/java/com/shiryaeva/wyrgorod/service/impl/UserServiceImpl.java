package com.shiryaeva.wyrgorod.service.impl;

import com.shiryaeva.wyrgorod.model.User;
import com.shiryaeva.wyrgorod.repository.UserRepository;
import com.shiryaeva.wyrgorod.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User findOne(Long id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(Long id, User request) {
        User existingUser = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        existingUser.setFirstName(request.getFirstName());
        existingUser.setLastName(request.getLastName());
        existingUser.setLogin(request.getLogin());
        existingUser.setPassword(request.getPassword());
        existingUser.setActivated(request.isActivated());
        existingUser.setEmail(request.getEmail());
        return userRepository.save(existingUser);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

}
