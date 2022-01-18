package com.shiryaeva.wyrgorod.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiryaeva.wyrgorod.model.Image;
import com.shiryaeva.wyrgorod.model.Publisher;
import com.shiryaeva.wyrgorod.repository.ImageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImageController.class)
public class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageRepository imageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockitoSession session;

    private static final String DEFAULT_NAME = "The times";
    private static final String UPDATED_NAME = "Back home";

    private static byte[] DEFAULT_CONTENT = new byte[0];

    static {
        try {
            DEFAULT_CONTENT = TestUtil.convertObjectToJsonBytes("content");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final String CONTENT_TYPE = "image/jpeg";

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Image createEntity() {
        Image image = new Image();
        image.setName(DEFAULT_NAME);
        image.setContentContentType(CONTENT_TYPE);
        image.setContent(DEFAULT_CONTENT);
        return image;
    }

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
    public void getImageById() throws Exception {
        MultipartFile file = new MockMultipartFile("Banana", "Banana.jpg", "text/plain", convertImage("/image/Velvet_Underground_and_Nico.jpg"));
        Image image = new Image(1l, file.getName(), file.getBytes(), CONTENT_TYPE);
        Optional<Image> imageOptional = Optional.of(image);
        given(imageRepository.findById(image.getId())).willReturn(imageOptional);
        this.mockMvc.perform(get("/image/1")
                        .contentType(MediaType.IMAGE_JPEG_VALUE)
                        .accept(MediaType.IMAGE_JPEG_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().bytes(image.getContent()));
    }

    private byte[] convertImage(String path) throws IOException {
        BufferedImage bImage = ImageIO.read(getClass().getResource(path));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", bos);
        return bos.toByteArray();
    }


}
