package com.shiryaeva.wyrgorod.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiryaeva.wyrgorod.model.Artist;
import com.shiryaeva.wyrgorod.repository.ArtistRepository;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArtistController.class)
public class ArtistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArtistRepository artistRepository;

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
    public void dummyArtistTest() throws Exception {
        RequestBuilder request = get("/dummy-artist")
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1,\"name\":\"Rick Astley\"}"))
                .andReturn();
    }

    @Test
    public void getAllArtists()
            throws Exception {

        Artist artist = new Artist("BG");

        List<Artist> allArtists = List.of(artist);

        given(artistRepository.findAll()).willReturn(allArtists);

        this.mockMvc.perform(get("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(artist.getName())))
                .andExpect(content().json(objectMapper.writeValueAsString(allArtists)));
    }

    @Test
    public void getEmptyListOfArtists() throws Exception {
        List<Artist> emptyList = new ArrayList<>();
        given(artistRepository.findAll()).willReturn(emptyList);
        this.mockMvc.perform(get("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getArtistById() throws Exception {
        Artist artist = new Artist(1L, "Coltrane");
        Optional<Artist> artistOptional = Optional.of(artist);
        given(artistRepository.findById(artist.getId())).willReturn(artistOptional);
        this.mockMvc.perform(get("/artist/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(artist)));
    }

    @Test
    public void postNewArtist() throws Exception {
        Artist artist = new Artist("dummy");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/artist")
                        .content(objectMapper.writeValueAsString(artist))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andReturn();
    }

    @Test
    public void updateArtist() throws Exception {
        Artist artist = new Artist(1L, "Coltrane");
        Optional<Artist> artistOptional = Optional.of(artist);
        given(artistRepository.findById(artist.getId())).willReturn(artistOptional);
        mockMvc.perform(MockMvcRequestBuilders.put("/artist/" + artist.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artist))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(artist)));

    }

    @Test
    public void deleteArtist() throws Exception {
        Artist artist = new Artist(1L, "Coltrane");
        Optional<Artist> artistOptional = Optional.of(artist);
        given(artistRepository.findById(artist.getId())).willReturn(artistOptional);
        mockMvc.perform(MockMvcRequestBuilders.delete("/artist/" + artist.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artist))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}
