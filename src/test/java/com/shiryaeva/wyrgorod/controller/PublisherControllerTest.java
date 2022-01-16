package com.shiryaeva.wyrgorod.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiryaeva.wyrgorod.model.Publisher;
import com.shiryaeva.wyrgorod.repository.PublisherRepository;
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
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PublisherController.class)
public class PublisherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublisherRepository publisherRepository;

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
    public void getAllPublishers()
            throws Exception {

        Publisher publisher = new Publisher(1L, "Geometriya Records");

        List<Publisher> allPublishers = List.of(publisher);

        given(publisherRepository.findAll()).willReturn(allPublishers);

        this.mockMvc.perform(get("/publishers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(publisher.getName())))
                .andExpect(content().json(objectMapper.writeValueAsString(allPublishers)));
    }

    @Test
    public void getEmptyListOfPublishers() throws Exception {
        List<Publisher> emptyList = new ArrayList<>();
        given(publisherRepository.findAll()).willReturn(emptyList);
        this.mockMvc.perform(get("/publishers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getPublisherById() throws Exception {
        Publisher publisher = new Publisher(1L, "Geometriya Records");
        Optional<Publisher> publisherOptional = Optional.of(publisher);
        given(publisherRepository.findById(publisher.getId())).willReturn(publisherOptional);
        this.mockMvc.perform(get("/publisher/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(publisher)));
    }

    @Test
    public void postNewPublisher() throws Exception {
        Publisher publisher = new Publisher(1L, "Geometriya Records");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/publisher")
                        .content(objectMapper.writeValueAsString(publisher))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andReturn();
    }

    @Test
    public void updatePublisher() throws Exception {
        Publisher publisher = new Publisher(1L, "Geometriya Records");
        Optional<Publisher> publisherOptional = Optional.of(publisher);
        given(publisherRepository.findById(publisher.getId())).willReturn(publisherOptional);
        mockMvc.perform(MockMvcRequestBuilders.put("/publisher/" + publisher.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publisher))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(publisher)));

    }

    @Test
    public void deletePublisher() throws Exception {
        Publisher publisher = new Publisher(1L, "Geometriya Records");
        Optional<Publisher> publisherOptional = Optional.of(publisher);
        given(publisherRepository.findById(publisher.getId())).willReturn(publisherOptional);
        mockMvc.perform(MockMvcRequestBuilders.delete("/publisher/" + publisher.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publisher))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}
