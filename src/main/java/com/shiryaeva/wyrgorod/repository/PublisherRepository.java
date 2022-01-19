package com.shiryaeva.wyrgorod.repository;

import com.shiryaeva.wyrgorod.model.Artist;
import com.shiryaeva.wyrgorod.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    Publisher findByNameLikeIgnoreCase(String name);
}