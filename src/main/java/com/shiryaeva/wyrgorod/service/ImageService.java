package com.shiryaeva.wyrgorod.service;

import com.shiryaeva.wyrgorod.model.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ImageService {
    Page<Image> findAll(Pageable pageable);

    Image findOne(Long id);

    Image save(Image image);

    Image update(Long id, Image request);

    void delete(Long id);

    List<Image> findAll();

}
