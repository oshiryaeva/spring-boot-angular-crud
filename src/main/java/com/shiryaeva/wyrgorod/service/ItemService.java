package com.shiryaeva.wyrgorod.service;

import com.shiryaeva.wyrgorod.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {

    Page<Item> findAll(Pageable pageable);

    Item findOne(Long id);

    Item save(Item artist);

    Item update(Long id, Item request);

    void delete(Long id);

    List<Item> findAll();

    List<Item> findByArtistId(Long id);

    List<Item> findByArtistName(String name);

    List<Item> findByPublisher(Long id);

    List<Item> findByPublisherName(String name);
}
