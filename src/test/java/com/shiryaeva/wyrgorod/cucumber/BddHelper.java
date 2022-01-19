package com.shiryaeva.wyrgorod.cucumber;

import com.shiryaeva.wyrgorod.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

public class BddHelper {
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private NewsItemRepository newsItemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private UserRepository userRepository;

    public void cleanUpDb() {
        userRepository.deleteAll();
        newsItemRepository.deleteAll();
        orderRepository.deleteAll();
        orderItemRepository.deleteAll();
        itemRepository.deleteAll();
        imageRepository.deleteAll();
        artistRepository.deleteAll();
        customerRepository.deleteAll();
        publisherRepository.deleteAll();
    }

}
