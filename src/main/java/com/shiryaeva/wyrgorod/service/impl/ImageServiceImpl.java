package com.shiryaeva.wyrgorod.service.impl;

import com.shiryaeva.wyrgorod.model.Image;
import com.shiryaeva.wyrgorod.repository.ImageRepository;
import com.shiryaeva.wyrgorod.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public Page<Image> findAll(Pageable pageable) {
        return imageRepository.findAll(pageable);
    }

    @Override
    public Image findOne(Long id) {
        return imageRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Image save(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public Image update(Long id, Image request) {
        Image existingImage = imageRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        existingImage.setName(request.getName());
        existingImage.setContent(request.getContent());
        return imageRepository.save(existingImage);
    }

    @Override
    public void delete(Long id) {
        imageRepository.deleteById(id);
    }

    @Override
    public List<Image> findAll() {
        return imageRepository.findAll();
    }
    
}
