package com.shiryaeva.wyrgorod.service;

import com.shiryaeva.wyrgorod.model.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PublisherService {
    Page<Publisher> findAll(Pageable pageable);

    Publisher findOne(Long id);

    Publisher save(Publisher publisher);

    Publisher update(Long id, Publisher request);

    void delete(Long id);

    List<Publisher> findAll();
}
