package com.shiryaeva.wyrgorod.service;

import com.shiryaeva.wyrgorod.model.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArtistService {
    Page<Artist> findAll(Pageable pageable);

    Artist findOne(Long id);

    Artist save(Artist artist);

    Artist update(Long id, Artist request);

    void delete(Long id);

    List<Artist> findAll();

    Artist findByName(String name);

    Artist updateName(String oldName, String newName);

    void deleteByName(String name);
}
