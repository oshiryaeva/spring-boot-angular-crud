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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
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
    public void getAllArtists() throws Exception {
        Artist artist = new Artist("BG");
        List<Artist> allArtists = List.of(artist);
        Page<Artist> page = new PageImpl<>(allArtists);
        Pageable pageable = PageRequest.of(0, 1);
        given(artistRepository.findAll(pageable)).willReturn(page);

        this.mockMvc.perform(get("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].name", is(artist.getName())))
                .andExpect(content().json(objectMapper.writeValueAsString(allArtists)));
    }

    @Test
    public void getEmptyListOfArtists() throws Exception {
        List<Artist> emptyList = new ArrayList<>();
        Page<Artist> emptyPage = new PageImpl<>(emptyList);
        Pageable pageable = PageRequest.of(0, 1);
        given(artistRepository.findAll(pageable)).willReturn(emptyPage);
        this.mockMvc.perform(get("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(emptyPage)));
    }

    @Test
    public void getArtistById() throws Exception {
        Artist artist = new Artist(1L, "Coltrane");
        Optional<Artist> artistOptional = Optional.of(artist);
        given(artistRepository.findById(artist.getId())).willReturn(artistOptional);
        this.mockMvc.perform(get("/artists/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(artist)));
    }

    @Test
    public void postNewArtist() throws Exception {
        Artist artist = new Artist("dummy");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/artists")
                        .content(objectMapper.writeValueAsString(artist))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    public void updateArtist() throws Exception {
        Artist artist = new Artist(1L, "Coltrane");
//        Optional<Artist> artistOptional = Optional.of(artist);
        given(artistRepository.existsById(artist.getId())).willReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.put("/artists/" + artist.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artist))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void deleteArtist() throws Exception {
        Artist artist = new Artist(1L, "Coltrane");
        Optional<Artist> artistOptional = Optional.of(artist);
        given(artistRepository.findById(artist.getId())).willReturn(artistOptional);
        mockMvc.perform(MockMvcRequestBuilders.delete("/artists/" + artist.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artist))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}
