package com.shiryaeva.wyrgorod.repository;

import com.shiryaeva.wyrgorod.model.NewsItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface NewsItemRepository extends JpaRepository<NewsItem, Long> {
    List<NewsItem> findByArtist_Id(Long id);

    List<NewsItem> findByArtist_NameIgnoreCase(String name);

    List<NewsItem> findByDate(Date date);
}