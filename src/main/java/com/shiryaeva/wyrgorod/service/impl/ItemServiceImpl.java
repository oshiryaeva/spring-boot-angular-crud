package com.shiryaeva.wyrgorod.service.impl;

import com.shiryaeva.wyrgorod.model.Item;
import com.shiryaeva.wyrgorod.repository.ItemRepository;
import com.shiryaeva.wyrgorod.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public class ItemServiceImpl implements ItemService {
    
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Page<Item> findAll(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    @Override
    public Item findOne(Long id) {
        return itemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Item update(Long id, Item request) {
        Item existingItem = itemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        existingItem.setTitle(request.getTitle());
        existingItem.setPublisher(request.getPublisher());
        existingItem.setPrice(request.getPrice());
        existingItem.setDescription(request.getDescription());
        existingItem.setImage(request.getImage());
        existingItem.setMedium(request.getMedium());
        existingItem.setArtist(request.getArtist());
        return itemRepository.save(existingItem);
    }

    @Override
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    public List<Item> findByArtistId(Long id) {
        return itemRepository.findByArtist_Id(id);
    }

    @Override
    public List<Item> findByArtistName(String name) {
        return itemRepository.findByArtist_NameIgnoreCase(name);
    }

    @Override
    public List<Item> findByPublisher(Long id) {
        return itemRepository.findByPublisher_Id(id);
    }

    @Override
    public List<Item> findByPublisherName(String name) {
        return itemRepository.findByPublisher_NameIgnoreCase(name);
    }
}
