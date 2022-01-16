package com.shiryaeva.wyrgorod.model;

import com.shiryaeva.wyrgorod.repository.ArtistRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ArtistRepositoryTest {

    @Autowired
    private ArtistRepository artistRepository;

    @Test
    public void testFindAll() {
        List<Artist> artists = artistRepository.findAll();
        assertEquals(5, artists.size());
    }

    @Test
    public void testFindOne() {
        Artist artist = artistRepository.findById(1L).get();
        assertEquals("Церковь Детства", artist.getName());
    }
}
