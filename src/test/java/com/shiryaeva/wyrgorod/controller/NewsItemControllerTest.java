package com.shiryaeva.wyrgorod.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiryaeva.wyrgorod.model.Artist;
import com.shiryaeva.wyrgorod.model.NewsItem;
import com.shiryaeva.wyrgorod.repository.NewsItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NewsItemController.class)
public class NewsItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsItemRepository newsItemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockitoSession session;

    @BeforeEach
    public void beforeMethod() {
        session = Mockito.mockitoSession()
                .initMocks(this)
                .startMocking();
    }

    @AfterEach
    public void afterMethod() {
        session.finishMocking();
    }

    @Test
    public void getAllNewsItems()
            throws Exception {
        Artist artist = new Artist(1L, "Tsoy");
        NewsItem newsItem = new NewsItem(1L, new Date(), "Breaking news", "Tsoy zhiv", artist);

        List<NewsItem> allNewsItems = List.of(newsItem);

        given(newsItemRepository.findAll()).willReturn(allNewsItems);

        this.mockMvc.perform(get("/newsItems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().json(objectMapper.writeValueAsString(allNewsItems)));
    }

    @Test
    public void getEmptyListOfNewsItems() throws Exception {
        List<NewsItem> emptyList = new ArrayList<>();
        given(newsItemRepository.findAll()).willReturn(emptyList);
        this.mockMvc.perform(get("/newsItems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getNewsItemById() throws Exception {
        Artist artist = new Artist(1L, "Tsoy");
        NewsItem newsItem = new NewsItem(1L, new Date(), "Breaking news", "Tsoy zhiv", artist);
        Optional<NewsItem> newsItemOptional = Optional.of(newsItem);
        given(newsItemRepository.findById(newsItem.getId())).willReturn(newsItemOptional);
        this.mockMvc.perform(get("/newsItem/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(newsItem)));
    }

    @Test
    public void postNewNewsItem() throws Exception {
        Artist artist = new Artist(1L, "Tsoy");
        NewsItem newsItem = new NewsItem(1L, new Date(), "Breaking news", "Tsoy zhiv", artist);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/newsItem")
                        .content(objectMapper.writeValueAsString(newsItem))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andReturn();
    }

    @Test
    public void updateNewsItem() throws Exception {
        Artist artist = new Artist(1L, "Tsoy");
        NewsItem newsItem = new NewsItem(1L, new Date(), "Breaking news", "Tsoy zhiv", artist);
        Optional<NewsItem> newsItemOptional = Optional.of(newsItem);
        given(newsItemRepository.findById(newsItem.getId())).willReturn(newsItemOptional);
        mockMvc.perform(MockMvcRequestBuilders.put("/newsItem/" + newsItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newsItem))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(newsItem)));
    }

    @Test
    public void deleteNewsItem() throws Exception {
        Artist artist = new Artist(1L, "Tsoy");
        NewsItem newsItem = new NewsItem(1L, new Date(), "Breaking news", "Tsoy zhiv", artist);
        Optional<NewsItem> newsItemOptional = Optional.of(newsItem);
        given(newsItemRepository.findById(newsItem.getId())).willReturn(newsItemOptional);
        mockMvc.perform(MockMvcRequestBuilders.delete("/newsItem/" + newsItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newsItem))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}
