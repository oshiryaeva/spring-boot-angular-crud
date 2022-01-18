package com.shiryaeva.wyrgorod.controller;

import com.shiryaeva.wyrgorod.IntegrationTest;
import com.shiryaeva.wyrgorod.exception.BadRequestAlertException;
import com.shiryaeva.wyrgorod.model.NewsItem;
import com.shiryaeva.wyrgorod.repository.NewsItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
public class NewsItemControllerTest {

    private static final String DEFAULT_TITLE = "No news";
    private static final String UPDATED_TITLE = "Good news";

    private static final String DEFAULT_DESCRIPTION = "DEFAULT_DESCRIPTION";
    private static final String UPDATED_DESCRIPTION = "UPDATED_DESCRIPTION";

    private static final Date DEFAULT_DATE = new GregorianCalendar(2022, 0 , 20).getTime();
    private static final Date UPDATED_DATE = new GregorianCalendar(2022, 0 , 22).getTime();

    private static final String ENTITY_API_URL = "/newsItems";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final MediaType CONTENT_TYPE = new MediaType("newsItem", "hal+json", StandardCharsets.UTF_8);
    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private NewsItemRepository newsItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNewsItemMockMvc;

    private NewsItem newsItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NewsItem createEntity(EntityManager em) {
        NewsItem newsItem = new NewsItem();
        newsItem.setTitle(DEFAULT_TITLE);
        newsItem.setDate(DEFAULT_DATE);
        newsItem.setDescription(DEFAULT_DESCRIPTION);
        return newsItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NewsItem createUpdatedEntity(EntityManager em) {
        NewsItem newsItem = new NewsItem();
        newsItem.setTitle(UPDATED_TITLE);
        newsItem.setDescription(UPDATED_DESCRIPTION);
        newsItem.setDate(UPDATED_DATE);
        return newsItem;
    }

    @BeforeEach
    public void initTest() {
        newsItem = createEntity(em);
    }

    @Test
    @Transactional
    void createNewsItem() throws Exception {
        int databaseSizeBeforeCreate = newsItemRepository.findAll().size();
        // Create the NewsItem
        restNewsItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(newsItem)))
                .andExpect(status().isCreated());

        // Validate the NewsItem in the database
        List<NewsItem> newsItemList = newsItemRepository.findAll();
        assertThat(newsItemList).hasSize(databaseSizeBeforeCreate + 1);
        NewsItem testNewsItem = newsItemList.get(newsItemList.size() - 1);
        assertThat(testNewsItem.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testNewsItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testNewsItem.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createNewsItemWithExistingId() throws Exception {
        // Create the NewsItem with an existing ID
        newsItem.setId(1L);

        int databaseSizeBeforeCreate = newsItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNewsItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(newsItem)))
                .andExpect(status().isBadRequest());

        // Validate the NewsItem in the database
        List<NewsItem> newsItemList = newsItemRepository.findAll();
        assertThat(newsItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = newsItemRepository.findAll().size();
        // set the field null
        newsItem.setTitle(null);

        // Create the NewsItem, which fails.

        restNewsItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(newsItem)))
                .andExpect(status().isBadRequest());

        List<NewsItem> newsItemList = newsItemRepository.findAll();
        assertThat(newsItemList).hasSize(databaseSizeBeforeTest);
    }


    @Test
    @Transactional
    void getAllNewsItems() throws Exception {
        // Initialize the database
        newsItemRepository.saveAndFlush(newsItem);

        // Get all the newsItemList
        restNewsItemMockMvc
                .perform(get(ENTITY_API_URL + "?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(newsItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE)));
    }

    @Test
    @Transactional
    void getNewsItem() throws Exception {
        // Initialize the database
        newsItemRepository.saveAndFlush(newsItem);

        // Get the newsItem
        restNewsItemMockMvc
                .perform(get(ENTITY_API_URL_ID, newsItem.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(newsItem.getId().intValue()))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingNewsItem() throws Exception {
        // Get the newsItem
        restNewsItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNewsItem() throws Exception {
        // Initialize the database
        newsItemRepository.saveAndFlush(newsItem);

        int databaseSizeBeforeUpdate = newsItemRepository.findAll().size();

        // Update the newsItem
        NewsItem updatedNewsItem = newsItemRepository.findById(newsItem.getId()).get();
        // Disconnect from session so that the updates on updatedNewsItem are not directly saved in db
        em.detach(updatedNewsItem);
        updatedNewsItem.setTitle(UPDATED_TITLE);
        updatedNewsItem.setDescription(UPDATED_DESCRIPTION);
        updatedNewsItem.setDate(UPDATED_DATE);

        restNewsItemMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, updatedNewsItem.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(updatedNewsItem))
                )
                .andExpect(status().isOk());

        // Validate the NewsItem in the database
        List<NewsItem> newsItemList = newsItemRepository.findAll();
        assertThat(newsItemList).hasSize(databaseSizeBeforeUpdate);
        NewsItem testNewsItem = newsItemList.get(newsItemList.size() - 1);
        assertThat(testNewsItem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testNewsItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testNewsItem.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingNewsItem() throws Exception {
        int databaseSizeBeforeUpdate = newsItemRepository.findAll().size();
        newsItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNewsItemMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, newsItem.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(newsItem))
                )
                .andExpect(status().isBadRequest());

        // Validate the NewsItem in the database
        List<NewsItem> newsItemList = newsItemRepository.findAll();
        assertThat(newsItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNewsItem() throws Exception {
        int databaseSizeBeforeUpdate = newsItemRepository.findAll().size();
        newsItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNewsItemMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, count.incrementAndGet())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(newsItem))
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestAlertException));

        // Validate the NewsItem in the database
        List<NewsItem> newsItemList = newsItemRepository.findAll();
        assertThat(newsItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNewsItem() throws Exception {
        int databaseSizeBeforeUpdate = newsItemRepository.findAll().size();
        newsItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNewsItemMockMvc
                .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(newsItem)))
                .andExpect(status().isMethodNotAllowed());

        // Validate the NewsItem in the database
        List<NewsItem> newsItemList = newsItemRepository.findAll();
        assertThat(newsItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNewsItemWithPatch() throws Exception {
        // Initialize the database
        newsItemRepository.saveAndFlush(newsItem);

        int databaseSizeBeforeUpdate = newsItemRepository.findAll().size();

        // Update the newsItem using partial update
        NewsItem partialUpdatedNewsItem = new NewsItem();
        partialUpdatedNewsItem.setId(newsItem.getId());

        partialUpdatedNewsItem.setTitle(UPDATED_TITLE);

        restNewsItemMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, partialUpdatedNewsItem.getId())
                                .contentType("application/merge-patch+json")
                                .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNewsItem))
                )
                .andExpect(status().isOk());

        // Validate the NewsItem in the database
        List<NewsItem> newsItemList = newsItemRepository.findAll();
        assertThat(newsItemList).hasSize(databaseSizeBeforeUpdate);
        NewsItem testNewsItem = newsItemList.get(newsItemList.size() - 1);
        assertThat(testNewsItem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testNewsItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testNewsItem.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateNewsItemWithPatch() throws Exception {
        // Initialize the database
        newsItemRepository.saveAndFlush(newsItem);

        int databaseSizeBeforeUpdate = newsItemRepository.findAll().size();

        // Update the newsItem using partial update
        NewsItem partialUpdatedNewsItem = new NewsItem();
        partialUpdatedNewsItem.setId(newsItem.getId());

        partialUpdatedNewsItem.setTitle(UPDATED_TITLE);
        partialUpdatedNewsItem.setDescription(UPDATED_DESCRIPTION);
        partialUpdatedNewsItem.setDate(UPDATED_DATE);

        restNewsItemMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, partialUpdatedNewsItem.getId())
                                .contentType("application/merge-patch+json")
                                .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNewsItem))
                )
                .andExpect(status().isOk());

        // Validate the NewsItem in the database
        List<NewsItem> newsItemList = newsItemRepository.findAll();
        assertThat(newsItemList).hasSize(databaseSizeBeforeUpdate);
        NewsItem testNewsItem = newsItemList.get(newsItemList.size() - 1);
        assertThat(testNewsItem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testNewsItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testNewsItem.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingNewsItem() throws Exception {
        int databaseSizeBeforeUpdate = newsItemRepository.findAll().size();
        newsItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNewsItemMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, newsItem.getId())
                                .contentType("application/merge-patch+json")
                                .content(TestUtil.convertObjectToJsonBytes(newsItem))
                )
                .andExpect(status().isBadRequest());

        // Validate the NewsItem in the database
        List<NewsItem> newsItemList = newsItemRepository.findAll();
        assertThat(newsItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNewsItem() throws Exception {
        int databaseSizeBeforeUpdate = newsItemRepository.findAll().size();
        newsItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNewsItemMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, count.incrementAndGet())
                                .contentType("application/merge-patch+json")
                                .content(TestUtil.convertObjectToJsonBytes(newsItem))
                )
                .andExpect(status().isBadRequest());

        // Validate the NewsItem in the database
        List<NewsItem> newsItemList = newsItemRepository.findAll();
        assertThat(newsItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNewsItem() throws Exception {
        int databaseSizeBeforeUpdate = newsItemRepository.findAll().size();
        newsItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNewsItemMockMvc
                .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(newsItem)))
                .andExpect(status().isMethodNotAllowed());

        // Validate the NewsItem in the database
        List<NewsItem> newsItemList = newsItemRepository.findAll();
        assertThat(newsItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNewsItem() throws Exception {
        // Initialize the database
        newsItemRepository.saveAndFlush(newsItem);

        int databaseSizeBeforeDelete = newsItemRepository.findAll().size();

        // Delete the newsItem
        restNewsItemMockMvc
                .perform(delete(ENTITY_API_URL_ID, newsItem.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NewsItem> newsItemList = newsItemRepository.findAll();
        assertThat(newsItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

}
