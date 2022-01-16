package com.shiryaeva.wyrgorod.service.impl;

import com.shiryaeva.wyrgorod.model.NewsItem;
import com.shiryaeva.wyrgorod.repository.NewsItemRepository;
import com.shiryaeva.wyrgorod.service.NewsItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;

public class NewsItemServiceImpl implements NewsItemService {
    
    @Autowired
    private NewsItemRepository newsItemRepository;

    @Override
    public Page<NewsItem> findAll(Pageable pageable) {
        return newsItemRepository.findAll(pageable);
    }

    @Override
    public NewsItem findOne(Long id) {
        return newsItemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public NewsItem save(NewsItem newsNewsItem) {
        return newsItemRepository.save(newsNewsItem);
    }

    @Override
    public NewsItem update(Long id, NewsItem request) {
        NewsItem existingNewsItem = newsItemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        existingNewsItem.setTitle(request.getTitle());
        existingNewsItem.setDescription(request.getDescription());
        existingNewsItem.setArtist(request.getArtist());
        existingNewsItem.setDate(request.getDate());
        return newsItemRepository.save(existingNewsItem);
    }

    @Override
    public void delete(Long id) {
        newsItemRepository.deleteById(id);
    }

    @Override
    public List<NewsItem> findAll() {
        return newsItemRepository.findAll();
    }

    @Override
    public List<NewsItem> findByArtistId(Long id) {
        return newsItemRepository.findByArtist_Id(id);
    }

    @Override
    public List<NewsItem> findByArtistName(String name) {
        return newsItemRepository.findByArtist_NameIgnoreCase(name);
    }

    @Override
    public List<NewsItem> findByDate(Date date) {
        return newsItemRepository.findByDate(date);
    }
}
