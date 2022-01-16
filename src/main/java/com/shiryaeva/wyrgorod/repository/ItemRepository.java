package com.shiryaeva.wyrgorod.repository;

import com.shiryaeva.wyrgorod.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByArtist_Id(Long id);

    List<Item> findByArtist_NameIgnoreCase(String name);

    List<Item> findByPublisher_Id(Long id);

    List<Item> findByPublisher_NameIgnoreCase(String name);

}