package com.shiryaeva.wyrgorod.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiryaeva.wyrgorod.model.Customer;
import com.shiryaeva.wyrgorod.repository.CustomerRepository;
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
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository customerRepository;

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
    public void getAllCustomers() throws Exception {

        Customer customer = new Customer("Henry", "Lee", "email");
        List<Customer> allCustomers = List.of(customer);
        given(customerRepository.findAll()).willReturn(allCustomers);

        this.mockMvc.perform(get("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().json(objectMapper.writeValueAsString(allCustomers)));
    }

    @Test
    public void getEmptyListOfCustomers() throws Exception {
        List<Customer> emptyList = new ArrayList<>();
        given(customerRepository.findAll()).willReturn(emptyList);
        this.mockMvc.perform(get("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getCustomerById() throws Exception {
        Customer customer = new Customer(1L, "Henry", "Lee", "email");
        Optional<Customer> customerOptional = Optional.of(customer);
        given(customerRepository.findById(customer.getId())).willReturn(customerOptional);
        this.mockMvc.perform(get("/customer/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(customer)));
    }

    @Test
    public void postNewCustomer() throws Exception {
        Customer customer = new Customer("Henry", "Lee", "email");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/customer")
                        .content(objectMapper.writeValueAsString(customer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andReturn();
    }

    @Test
    public void updateCustomer() throws Exception {
        Customer customer = new Customer(1L, "Henry", "Lee", "email");
        Optional<Customer> customerOptional = Optional.of(customer);
        given(customerRepository.findById(customer.getId())).willReturn(customerOptional);
        mockMvc.perform(MockMvcRequestBuilders.put("/customer/" + customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(customer)));

    }

    @Test
    public void deleteCustomer() throws Exception {
        Customer customer = new Customer(1L, "Henry", "Lee", "email");
        Optional<Customer> customerOptional = Optional.of(customer);
        given(customerRepository.findById(customer.getId())).willReturn(customerOptional);
        mockMvc.perform(MockMvcRequestBuilders.delete("/customer/" + customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}
