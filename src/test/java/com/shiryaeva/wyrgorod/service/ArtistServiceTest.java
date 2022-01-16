package com.shiryaeva.wyrgorod.service;

import com.shiryaeva.wyrgorod.model.Artist;
import com.shiryaeva.wyrgorod.repository.ArtistRepository;
import com.shiryaeva.wyrgorod.service.impl.ArtistServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ArtistServiceTest {

    @InjectMocks
    public ArtistServiceImpl artistService;

    @Mock
    public ArtistRepository artistRepository;

    @Test
    public void test() {
        long id = 2L;
        Artist expected = new Artist();
        expected.setId(id);
        when (artistRepository.findById(id)).thenReturn(Optional.of(expected));
        var res = artistService.findOne(id);
        Assertions.assertEquals(id, res.getId());
    }

    @Test
    public void test2() {
        long id = 2L;
        when (artistRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            var res = artistService.findOne(id);
        });
    }

    @Test
    public void test3() {
        long id = 2L;
        Artist expected = new Artist();
        expected.setId(id);
        when (artistRepository.findById(any())).thenReturn(Optional.of(expected));
        var res = artistService.findOne(id);
        Assertions.assertEquals(id, res.getId());
    }
}