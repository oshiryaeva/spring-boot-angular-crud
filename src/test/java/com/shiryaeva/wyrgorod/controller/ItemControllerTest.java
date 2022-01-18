package com.shiryaeva.wyrgorod.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiryaeva.wyrgorod.IntegrationTest;
import com.shiryaeva.wyrgorod.exception.BadRequestAlertException;
import com.shiryaeva.wyrgorod.model.*;
import com.shiryaeva.wyrgorod.repository.ArtistRepository;
import com.shiryaeva.wyrgorod.repository.ImageRepository;
import com.shiryaeva.wyrgorod.repository.ItemRepository;
import com.shiryaeva.wyrgorod.repository.PublisherRepository;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
public class ItemControllerTest {

    private static final String CONTENT_TYPE = "image/jpeg";

    private static final String DEFAULT_TITLE = "The Times They Are a-Changin";
    private static final String UPDATED_TITLE = "Bringing It All Back Home";

    private static final String DEFAULT_DESCRIPTION = "The album consists mostly of stark, sparsely arranged ballads concerning issues such as racism, poverty, and social change. The title track is one of Dylan's most famous; many feel that it captures the spirit of social and political upheaval that characterized the 1960s.";
    private static final String UPDATED_DESCRIPTION = "The first half of the album features electric songs, followed by mainly acoustic songs in the second half. The album abandons the protest music of Dylan's previous records in favor of more surreal, complex lyrics.";

    private static final Artist DEFAULT_ARTIST = ArtistControllerTest.createEntity();
    private static final Artist UPDATED_ARTIST = ArtistControllerTest.createUpdatedEntity();
    
    private static final Publisher DEFAULT_PUBLISHER = PublisherControllerTest.createEntity();
    private static final Publisher UPDATED_PUBLISHER = PublisherControllerTest.createUpdatedEntity();
    
    private static final Image DEFAULT_IMAGE = ImageControllerTest.createEntity();

    private static final Medium DEFAULT_MEDIUM = Medium.CD;
    private static final Medium UPDATED_MEDIUM = Medium.LP;
    
    private static final BigDecimal DEFAULT_PRICE = BigDecimal.ONE;
    private static final BigDecimal UPDATED_PRICE = BigDecimal.TEN;

    private static final String ENTITY_API_URL = "/items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    private Item item;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemMockMvc;


    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createEntity() {
        Item item = new Item();
        item.setTitle(DEFAULT_TITLE);
        item.setDescription(DEFAULT_DESCRIPTION);
        item.setMedium(DEFAULT_MEDIUM);
        item.setPrice(DEFAULT_PRICE);
//        item.setArtist(DEFAULT_ARTIST);
//        item.setPublisher(DEFAULT_PUBLISHER);
//        item.setImage(DEFAULT_IMAGE);
        return item;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createUpdatedEntity() {
        Item item = new Item();
//        item.setArtist(UPDATED_ARTIST);
        item.setTitle(UPDATED_TITLE);
        item.setDescription(UPDATED_DESCRIPTION);
//        item.setPublisher(UPDATED_PUBLISHER);
        item.setMedium(UPDATED_MEDIUM);
        item.setPrice(UPDATED_PRICE);
//        item.setImage(DEFAULT_IMAGE);
        return item;
    }
    
    @BeforeEach
    public void beforeMethod() {
        item = createEntity();
    }

    @Test
    @Transactional
    void createItem() throws Exception {
        int databaseSizeBeforeCreate = itemRepository.findAll().size();
        // Create the Item
        restItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isCreated());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeCreate + 1);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testItem.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testItem.getMedium()).isEqualTo(DEFAULT_MEDIUM);
    }

    @Test
    @Transactional
    void createItemWithExistingId() throws Exception {
        // Create the Item with an existing ID
        item.setId(1L);

        int databaseSizeBeforeCreate = itemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setTitle(null);

        // Create the Item, which fails.

        restItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllItems() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList
        restItemMockMvc
                .perform(get(ENTITY_API_URL + "?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)));
    }

    @Test
    @Transactional
    void getItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get the item
        restItemMockMvc
                .perform(get(ENTITY_API_URL_ID, item.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(item.getId().intValue()))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)));
    }

    @Test
    @Transactional
    void getNonExistingItem() throws Exception {
        // Get the item
        restItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item
        Item updatedItem = itemRepository.findById(item.getId()).get();
        // Disconnect from session so that the updates on updatedItem are not directly saved in db
        em.detach(updatedItem);
        updatedItem.setTitle(UPDATED_TITLE);
        updatedItem.setDescription(UPDATED_DESCRIPTION);
        updatedItem.setPrice(UPDATED_PRICE);

        restItemMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, updatedItem.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(updatedItem))
                )
                .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testItem.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, item.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(item))
                )
                .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, count.incrementAndGet())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(item))
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestAlertException));

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
                .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isMethodNotAllowed());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemWithPatch() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item using partial update
        Item partialUpdatedItem = new Item();
        partialUpdatedItem.setId(item.getId());

        partialUpdatedItem.setDescription(UPDATED_DESCRIPTION);

        restItemMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, partialUpdatedItem.getId())
                                .contentType("application/merge-patch+json")
                                .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItem))
                )
                .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testItem.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateItemWithPatch() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item using partial update
        Item partialUpdatedItem = new Item();
        partialUpdatedItem.setId(item.getId());

        partialUpdatedItem.setTitle(UPDATED_TITLE);
        partialUpdatedItem.setDescription(UPDATED_DESCRIPTION);
        partialUpdatedItem.setPrice(UPDATED_PRICE);
        partialUpdatedItem.setMedium(UPDATED_MEDIUM);
        partialUpdatedItem.setArtist(UPDATED_ARTIST);
        partialUpdatedItem.setPublisher(UPDATED_PUBLISHER);

        restItemMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, partialUpdatedItem.getId())
                                .contentType("application/merge-patch+json")
                                .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItem))
                )
                .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testItem.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, item.getId())
                                .contentType("application/merge-patch+json")
                                .content(TestUtil.convertObjectToJsonBytes(item))
                )
                .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, count.incrementAndGet())
                                .contentType("application/merge-patch+json")
                                .content(TestUtil.convertObjectToJsonBytes(item))
                )
                .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
                .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isMethodNotAllowed());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        int databaseSizeBeforeDelete = itemRepository.findAll().size();

        // Delete the item
        restItemMockMvc
                .perform(delete(ENTITY_API_URL_ID, item.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeDelete - 1);
    }

}
