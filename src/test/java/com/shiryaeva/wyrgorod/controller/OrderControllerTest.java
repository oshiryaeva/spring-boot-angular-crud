package com.shiryaeva.wyrgorod.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiryaeva.wyrgorod.model.*;
import com.shiryaeva.wyrgorod.repository.OrderRepository;
import com.shiryaeva.wyrgorod.service.OrderService;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockitoSession session;

    private static final String CONTENT_TYPE = "image/jpeg";

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
    public void getAllOrders() throws Exception {
        Order order = createDummyOrder();
        List<Order> allOrders = List.of(order);
        given(orderRepository.findAll()).willReturn(allOrders);
        this.mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().json(objectMapper.writeValueAsString(allOrders)));
    }

    @Test
    public void getEmptyListOfOrders() throws Exception {
        List<Order> emptyList = new ArrayList<>();
        given(orderRepository.findAll()).willReturn(emptyList);
        this.mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getOrderById() throws Exception {
        Order order = createDummyOrder();
        Optional<Order> orderOptional = Optional.of(order);
        given(orderRepository.findById(order.getId())).willReturn(orderOptional);
        this.mockMvc.perform(get("/order/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(order)));
    }

/*    @Test
    public void postNewOrder() throws Exception {
        Order order = createDummyOrder();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .content(objectMapper.writeValueAsString(order))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andReturn();
    }*/

/*    @Test
    public void updateOrder() throws Exception {
        Order order = createDummyOrder();
        Optional<Order> orderOptional = Optional.of(order);
        given(orderRepository.findById(order.getId())).willReturn(orderOptional);
        mockMvc.perform(MockMvcRequestBuilders.put("/order/" + order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(order)));
    }*/

    @Test
    public void deleteOrder() throws Exception {
        Order order = createDummyOrder();
        Optional<Order> orderOptional = Optional.of(order);
        given(orderRepository.findById(order.getId())).willReturn(orderOptional);
        mockMvc.perform(MockMvcRequestBuilders.delete("/order/" + order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    private Order createDummyOrder() throws IOException {
        Order order = new Order();
        order.setId(1L);
        order.setCustomer(new Customer(1L, "Henry", "Lee", "email"));
        OrderItem orderItem = new OrderItem(1L, createMockItem(), 2, order);
        List<OrderItem> orderItems = List.of(orderItem);
        order.setOrderItems(orderItems);
        order.setAmount(orderService.calculateOrderAmount(order));
        return order;
    }

    private Item createMockItem() throws IOException {
        String title = "The Velvet Underground & Nico";
        String description = "The Velvet Underground & Nico is the debut album by American rock band the Velvet " +
                "Underground and German singer Nico, released in March 1967 through Verve Records. It was recorded " +
                "in 1966 while the band were featured on Andy Warhol's Exploding Plastic Inevitable tour. The album " +
                "features experimental performance sensibilities and controversial lyrical topics, including drug abuse, " +
                "prostitution, sadomasochism and sexual deviancy. It sold poorly and was mostly ignored by contemporary " +
                "critics, but later became regarded as one of the most influential albums in the history of rock and pop " +
                "music. " +
                "All songs written by Lou Reed, except where noted." +
                "Side 1\n" +
                "1.\t\"Sunday Morning\"\t\n" +
                "2.\t\"I'm Waiting for the Man\"\n" +
                "3.\t\"Femme Fatale\"\n" +
                "4.\t\"Venus in Furs\"\n" +
                "5.\t\"Run Run Run\"\n" +
                "6.\t\"All Tomorrow's Parties\"\n" +
                "Side 2\n" +
                "1.\t\"Heroin\"\n" +
                "2.\t\"There She Goes Again\"\n" +
                "3.\t\"I'll Be Your Mirror\"\n" +
                "4.\t\"The Black Angel's Death Song\"\t\n" +
                "5.\t\"European Son\"";
        Artist artist = new Artist(1L, "The Velvet Underground");
        Publisher publisher = new Publisher(1L, "Verve");
        MultipartFile file = new MockMultipartFile("Banana", "Banana.jpg", "text/plain",
                convertImage("/image/Velvet_Underground_and_Nico.jpg"));
        Image image = new Image(1l, file.getName(), file.getBytes(), CONTENT_TYPE);
        return new Item(1l, title, description, artist, publisher, BigDecimal.TEN, Medium.CD, image);
    }

    private byte[] convertImage(String path) throws IOException {
        BufferedImage bImage = ImageIO.read(getClass().getResource(path));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", bos);
        return bos.toByteArray();
    }

}
