package com.shiryaeva.wyrgorod.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiryaeva.wyrgorod.IntegrationTest;
import com.shiryaeva.wyrgorod.exception.BadRequestAlertException;
import com.shiryaeva.wyrgorod.model.Artist;
import com.shiryaeva.wyrgorod.model.Customer;
import com.shiryaeva.wyrgorod.repository.ArtistRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
public class ArtistControllerTest {

    private static final String DEFAULT_NAME = "Robert Zimmerman";
    private static final String UPDATED_NAME = "Bob Dylan";

    private static final String ENTITY_API_URL = "/artists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArtistMockMvc;

    private Artist artist;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Artist createEntity() {
        Artist artist = new Artist();
        artist.setName(DEFAULT_NAME);
        return artist;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Artist createUpdatedEntity() {
        Artist artist = new Artist();
        artist.setName(UPDATED_NAME);
        return artist;
    }

    @BeforeEach
    public void initTest() {
        artist = createEntity();
    }

    @Test
    @Transactional
    void createArtist() throws Exception {
        int databaseSizeBeforeCreate = artistRepository.findAll().size();
        // Create the Artist
        restArtistMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(artist)))
                .andExpect(status().isCreated());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeCreate + 1);
        Artist testArtist = artistList.get(artistList.size() - 1);
        assertThat(testArtist.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createArtistWithExistingId() throws Exception {
        // Create the Artist with an existing ID
        artist.setId(1L);

        int databaseSizeBeforeCreate = artistRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArtistMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(artist)))
                .andExpect(status().isBadRequest());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = artistRepository.findAll().size();
        // set the field null
        artist.setName(null);

        // Create the Artist, which fails.

        restArtistMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(artist)))
                .andExpect(status().isBadRequest());

        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeTest);
    }


    @Test
    @Transactional
    void getAllArtists() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList
        restArtistMockMvc
                .perform(get(ENTITY_API_URL + "?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(artist.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getArtist() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get the artist
        restArtistMockMvc
                .perform(get(ENTITY_API_URL_ID, artist.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(artist.getId().intValue()))
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingArtist() throws Exception {
        // Get the artist
        restArtistMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewArtist() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        int databaseSizeBeforeUpdate = artistRepository.findAll().size();

        // Update the artist
        Artist updatedArtist = artistRepository.findById(artist.getId()).get();
        // Disconnect from session so that the updates on updatedArtist are not directly saved in db
        em.detach(updatedArtist);
        updatedArtist.setName(UPDATED_NAME);

        restArtistMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, updatedArtist.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(updatedArtist))
                )
                .andExpect(status().isOk());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeUpdate);
        Artist testArtist = artistList.get(artistList.size() - 1);
        assertThat(testArtist.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingArtist() throws Exception {
        int databaseSizeBeforeUpdate = artistRepository.findAll().size();
        artist.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArtistMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, artist.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(artist))
                )
                .andExpect(status().isBadRequest());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArtist() throws Exception {
        int databaseSizeBeforeUpdate = artistRepository.findAll().size();
        artist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArtistMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, count.incrementAndGet())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(artist))
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestAlertException));

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArtist() throws Exception {
        int databaseSizeBeforeUpdate = artistRepository.findAll().size();
        artist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArtistMockMvc
                .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(artist)))
                .andExpect(status().isMethodNotAllowed());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void fullUpdateArtistWithPatch() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        int databaseSizeBeforeUpdate = artistRepository.findAll().size();

        // Update the artist using partial update
        Artist partialUpdatedArtist = new Artist();
        partialUpdatedArtist.setId(artist.getId());
        partialUpdatedArtist.setName(UPDATED_NAME);

        restArtistMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, partialUpdatedArtist.getId())
                                .contentType("application/merge-patch+json")
                                .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArtist))
                )
                .andExpect(status().isOk());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeUpdate);
        Artist testArtist = artistList.get(artistList.size() - 1);
        assertThat(testArtist.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingArtist() throws Exception {
        int databaseSizeBeforeUpdate = artistRepository.findAll().size();
        artist.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArtistMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, artist.getId())
                                .contentType("application/merge-patch+json")
                                .content(TestUtil.convertObjectToJsonBytes(artist))
                )
                .andExpect(status().isBadRequest());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArtist() throws Exception {
        int databaseSizeBeforeUpdate = artistRepository.findAll().size();
        artist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArtistMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, count.incrementAndGet())
                                .contentType("application/merge-patch+json")
                                .content(TestUtil.convertObjectToJsonBytes(artist))
                )
                .andExpect(status().isBadRequest());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArtist() throws Exception {
        int databaseSizeBeforeUpdate = artistRepository.findAll().size();
        artist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArtistMockMvc
                .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(artist)))
                .andExpect(status().isMethodNotAllowed());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArtist() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        int databaseSizeBeforeDelete = artistRepository.findAll().size();

        // Delete the artist
        restArtistMockMvc
                .perform(delete(ENTITY_API_URL_ID, artist.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeDelete - 1);
    }

}
