package com.shiryaeva.wyrgorod.controller;

import com.shiryaeva.wyrgorod.IntegrationTest;
import com.shiryaeva.wyrgorod.exception.BadRequestAlertException;
import com.shiryaeva.wyrgorod.model.Customer;
import com.shiryaeva.wyrgorod.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
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
class CustomerControllerTest {

    private static final String DEFAULT_LAST_NAME = "Dylan";
    private static final String UPDATED_LAST_NAME = "Zimmerman";

    private static final String DEFAULT_FIRST_NAME = "Bob";
    private static final String UPDATED_FIRST_NAME = "Robert";

    private static final String DEFAULT_EMAIL = "DEFAULT_EMAIL";
    private static final String UPDATED_EMAIL = "UPDATED_EMAIL";

    private static final String ENTITY_API_URL = "/customers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerMockMvc;

    private Customer customer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createEntity(EntityManager em) {
        Customer customer = new Customer();
        customer.setLastName(DEFAULT_LAST_NAME);
        customer.setFirstName(DEFAULT_FIRST_NAME);
        customer.setEmail(DEFAULT_EMAIL);
        return customer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createUpdatedEntity(EntityManager em) {
        Customer customer = new Customer();
        customer.setLastName(UPDATED_LAST_NAME);
        customer.setFirstName(UPDATED_FIRST_NAME);
        customer.setEmail(UPDATED_EMAIL);
        return customer;
    }

    @BeforeEach
    public void initTest() {
        customer = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();
        // Create the Customer
        restCustomerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customer)))
                .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCustomer.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testCustomer.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void createCustomerWithExistingId() throws Exception {
        // Create the Customer with an existing ID
        customer.setId(1L);

        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customer)))
                .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setLastName(null);

        // Create the Customer, which fails.

        restCustomerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customer)))
                .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setFirstName(null);

        // Create the Customer, which fails.

        restCustomerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customer)))
                .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList
        restCustomerMockMvc
                .perform(get(ENTITY_API_URL + "?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get the customer
        restCustomerMockMvc
                .perform(get(ENTITY_API_URL_ID, customer.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(customer.getId().intValue()))
                .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
                .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
                .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        // Disconnect from session so that the updates on updatedCustomer are not directly saved in db
        em.detach(updatedCustomer);
        updatedCustomer.setLastName(UPDATED_LAST_NAME);
        updatedCustomer.setFirstName(UPDATED_FIRST_NAME);
        updatedCustomer.setEmail(UPDATED_EMAIL);

        restCustomerMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, updatedCustomer.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(updatedCustomer))
                )
                .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCustomer.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCustomer.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, customer.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(customer))
                )
                .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
            restCustomerMockMvc
                    .perform(
                            put(ENTITY_API_URL_ID, count.incrementAndGet())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(TestUtil.convertObjectToJsonBytes(customer))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestAlertException));

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
                .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customer)))
                .andExpect(status().isMethodNotAllowed());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer.setLastName(UPDATED_LAST_NAME);

        restCustomerMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
                                .contentType("application/merge-patch+json")
                                .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomer))
                )
                .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCustomer.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testCustomer.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer.setLastName(UPDATED_LAST_NAME);
        partialUpdatedCustomer.setFirstName(UPDATED_FIRST_NAME);
        partialUpdatedCustomer.setEmail(UPDATED_EMAIL);

        restCustomerMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
                                .contentType("application/merge-patch+json")
                                .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomer))
                )
                .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCustomer.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCustomer.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, customer.getId())
                                .contentType("application/merge-patch+json")
                                .content(TestUtil.convertObjectToJsonBytes(customer))
                )
                .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, count.incrementAndGet())
                                .contentType("application/merge-patch+json")
                                .content(TestUtil.convertObjectToJsonBytes(customer))
                )
                .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
                .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(customer)))
                .andExpect(status().isMethodNotAllowed());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Delete the customer
        restCustomerMockMvc
                .perform(delete(ENTITY_API_URL_ID, customer.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}