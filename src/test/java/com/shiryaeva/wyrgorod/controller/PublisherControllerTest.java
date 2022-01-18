package com.shiryaeva.wyrgorod.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiryaeva.wyrgorod.IntegrationTest;
import com.shiryaeva.wyrgorod.exception.BadRequestAlertException;
import com.shiryaeva.wyrgorod.model.Publisher;
import com.shiryaeva.wyrgorod.model.Publisher;
import com.shiryaeva.wyrgorod.repository.PublisherRepository;
import com.shiryaeva.wyrgorod.repository.PublisherRepository;
import io.cucumber.java8.De;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
public class PublisherControllerTest {

    private static final String DEFAULT_NAME = "Fuzz";
    private static final String UPDATED_NAME = "Moroz Records";

    private static final String ENTITY_API_URL = "/publishers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPublisherMockMvc;

    private Publisher publisher;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Publisher createEntity() {
        Publisher publisher = new Publisher();
        publisher.setName(DEFAULT_NAME);
        return publisher;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Publisher createUpdatedEntity() {
        Publisher publisher = new Publisher();
        publisher.setName(UPDATED_NAME);
        return publisher;
    }

    @BeforeEach
    public void initTest() {
        publisher = createEntity();
    }

    @Test
    @Transactional
    void createPublisher() throws Exception {
        int databaseSizeBeforeCreate = publisherRepository.findAll().size();
        // Create the Publisher
        restPublisherMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(publisher)))
                .andExpect(status().isCreated());

        // Validate the Publisher in the database
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeCreate + 1);
        Publisher testPublisher = publisherList.get(publisherList.size() - 1);
        assertThat(testPublisher.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createPublisherWithExistingId() throws Exception {
        // Create the Publisher with an existing ID
        publisher.setId(1L);

        int databaseSizeBeforeCreate = publisherRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPublisherMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(publisher)))
                .andExpect(status().isBadRequest());

        // Validate the Publisher in the database
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = publisherRepository.findAll().size();
        // set the field null
        publisher.setName(null);

        // Create the Publisher, which fails.

        restPublisherMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(publisher)))
                .andExpect(status().isBadRequest());

        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPublishers() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get all the publisherList
        restPublisherMockMvc
                .perform(get(ENTITY_API_URL + "?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(publisher.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getPublisher() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get the publisher
        restPublisherMockMvc
                .perform(get(ENTITY_API_URL_ID, publisher.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(publisher.getId().intValue()))
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingPublisher() throws Exception {
        // Get the publisher
        restPublisherMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPublisher() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        int databaseSizeBeforeUpdate = publisherRepository.findAll().size();

        // Update the publisher
        Publisher updatedPublisher = publisherRepository.findById(publisher.getId()).get();
        // Disconnect from session so that the updates on updatedPublisher are not directly saved in db
        em.detach(updatedPublisher);
        updatedPublisher.setName(UPDATED_NAME);

        restPublisherMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, updatedPublisher.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(updatedPublisher))
                )
                .andExpect(status().isOk());

        // Validate the Publisher in the database
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeUpdate);
        Publisher testPublisher = publisherList.get(publisherList.size() - 1);
        assertThat(testPublisher.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingPublisher() throws Exception {
        int databaseSizeBeforeUpdate = publisherRepository.findAll().size();
        publisher.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPublisherMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, publisher.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(publisher))
                )
                .andExpect(status().isBadRequest());

        // Validate the Publisher in the database
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPublisher() throws Exception {
        int databaseSizeBeforeUpdate = publisherRepository.findAll().size();
        publisher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublisherMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, count.incrementAndGet())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(publisher))
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestAlertException));

        // Validate the Publisher in the database
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPublisher() throws Exception {
        int databaseSizeBeforeUpdate = publisherRepository.findAll().size();
        publisher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublisherMockMvc
                .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(publisher)))
                .andExpect(status().isMethodNotAllowed());

        // Validate the Publisher in the database
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void fullUpdatePublisherWithPatch() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        int databaseSizeBeforeUpdate = publisherRepository.findAll().size();

        // Update the publisher using partial update
        Publisher partialUpdatedPublisher = new Publisher();
        partialUpdatedPublisher.setId(publisher.getId());

        partialUpdatedPublisher.setName(UPDATED_NAME);

        restPublisherMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, partialUpdatedPublisher.getId())
                                .contentType("application/merge-patch+json")
                                .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPublisher))
                )
                .andExpect(status().isOk());

        // Validate the Publisher in the database
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeUpdate);
        Publisher testPublisher = publisherList.get(publisherList.size() - 1);
        assertThat(testPublisher.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingPublisher() throws Exception {
        int databaseSizeBeforeUpdate = publisherRepository.findAll().size();
        publisher.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPublisherMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, publisher.getId())
                                .contentType("application/merge-patch+json")
                                .content(TestUtil.convertObjectToJsonBytes(publisher))
                )
                .andExpect(status().isBadRequest());

        // Validate the Publisher in the database
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPublisher() throws Exception {
        int databaseSizeBeforeUpdate = publisherRepository.findAll().size();
        publisher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublisherMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, count.incrementAndGet())
                                .contentType("application/merge-patch+json")
                                .content(TestUtil.convertObjectToJsonBytes(publisher))
                )
                .andExpect(status().isBadRequest());

        // Validate the Publisher in the database
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPublisher() throws Exception {
        int databaseSizeBeforeUpdate = publisherRepository.findAll().size();
        publisher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublisherMockMvc
                .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(publisher)))
                .andExpect(status().isMethodNotAllowed());

        // Validate the Publisher in the database
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePublisher() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        int databaseSizeBeforeDelete = publisherRepository.findAll().size();

        // Delete the publisher
        restPublisherMockMvc
                .perform(delete(ENTITY_API_URL_ID, publisher.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeDelete - 1);
    }

}
