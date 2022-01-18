package com.shiryaeva.wyrgorod.service.impl;

import com.shiryaeva.wyrgorod.model.Publisher;
import com.shiryaeva.wyrgorod.repository.PublisherRepository;
import com.shiryaeva.wyrgorod.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PublisherServiceImpl implements PublisherService {

    @Autowired
    private PublisherRepository publisherRepository;

    @Override
    public Page<Publisher> findAll(Pageable pageable) {
        return publisherRepository.findAll(pageable);
    }

    @Override
    public Publisher findOne(Long id) {
        return publisherRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Publisher save(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    @Override
    public Publisher update(Long id, Publisher request) {
        Publisher existingPublisher = publisherRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        existingPublisher.setName(request.getName());
        return publisherRepository.save(existingPublisher);
    }

    @Override
    public void delete(Long id) {
        publisherRepository.deleteById(id);

    }

    @Override
    public List<Publisher> findAll() {
        return publisherRepository.findAll();
    }
}
