package com.shiryaeva.wyrgorod.service;

import com.shiryaeva.wyrgorod.model.NewsItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface NewsItemService {

    Page<NewsItem> findAll(Pageable pageable);

    NewsItem findOne(Long id);

    NewsItem save(NewsItem artist);

    NewsItem update(Long id, NewsItem request);

    void delete(Long id);

    List<NewsItem> findAll();

    List<NewsItem> findByArtistId(Long id);

    List<NewsItem> findByArtistName(String name);

    List<NewsItem> findByDate(Date date);
}
