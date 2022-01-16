package com.shiryaeva.wyrgorod.repository;

import com.shiryaeva.wyrgorod.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

}