package com.shiryaeva.wyrgorod.service.impl;

import com.shiryaeva.wyrgorod.model.Artist;
import com.shiryaeva.wyrgorod.repository.ArtistRepository;
import com.shiryaeva.wyrgorod.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ArtistServiceImpl implements ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    @Override
    public Page<Artist> findAll(Pageable pageable) {
        return artistRepository.findAll(pageable);
    }

    @Override
    public Artist findOne(Long id) {
        return artistRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Artist save(Artist artist) {
        return artistRepository.save(artist);
    }

    @Override
    public Artist update(Long id, Artist request) {
        Artist existingArtist = artistRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        existingArtist.setName(request.getName());
        return artistRepository.save(existingArtist);
    }

    @Override
    public void delete(Long id) {
        artistRepository.deleteById(id);
    }

    @Override
    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    @Override
    public Artist findByName(String name) {
        return artistRepository.findByNameLikeIgnoreCase(name);
    }

    @Override
    public Artist updateName(String oldName, String newName) {
        artistRepository.updateName(oldName, newName);
        return artistRepository.findByNameLikeIgnoreCase(newName);
    }

    @Override
    public void deleteByName(String name) {
        artistRepository.deleteByNameLikeIgnoreCase(name);
    }
}
