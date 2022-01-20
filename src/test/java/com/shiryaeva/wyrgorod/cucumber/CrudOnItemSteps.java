package com.shiryaeva.wyrgorod.cucumber;

import com.shiryaeva.wyrgorod.model.Artist;
import com.shiryaeva.wyrgorod.model.Item;
import com.shiryaeva.wyrgorod.model.Medium;
import com.shiryaeva.wyrgorod.model.Publisher;
import com.shiryaeva.wyrgorod.repository.ArtistRepository;
import com.shiryaeva.wyrgorod.repository.PublisherRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@TestPropertySource(locations = {"classpath:application-test.properties"})
public class CrudOnItemSteps {

    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private PublisherRepository publisherRepository;

    private Artist artist;
    private Publisher publisher;
    private ResponseEntity<Item> responseEntity;
    private RestTemplate REST;
    private static final String URL = "http://localhost:8080/items";
    private static final String ENTITY_API_URL = "/items";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Before
    public void initializeFields() {
        REST = new RestTemplate();
    }

    @Given("database is empty")
    public void databaseIsEmpty() {
        BddHelper helper = new BddHelper();
        helper.cleanUpDb();
    }

    @Given("There is Artist {string}")
    public void thereIsArtist(String artistName) {
        if (artistRepository.findByNameLikeIgnoreCase(artistName) == null) {
            artist = new Artist(artistName);
            artistRepository.saveAndFlush(artist);
        } else
            artist = artistRepository.findByNameLikeIgnoreCase(artistName);
    }

    @And("there is Publisher {string}")
    public void thereIsPublisher(String publisherName) {
        if (publisherRepository.findByNameLikeIgnoreCase(publisherName) == null) {
            publisher = new Publisher(publisherName);
            publisherRepository.saveAndFlush(publisher);
        } else
            publisher = publisherRepository.findByNameLikeIgnoreCase(publisherName);
    }

    @When("I create item with title {string} and description {string} and price {int} and Medium {string}")
    public void iCreateItemWithTitleAndDescriptionAndPricePriceAndMedium(String title, String description, int price, String medium) {
        Item item = new Item();
        item.setId(count.incrementAndGet());
        item.setTitle(title);
        item.setDescription(description);
        item.setPrice(BigDecimal.valueOf(price));
        item.setMedium(Medium.valueOf(medium));
        item.setArtist(artist);
        item.setPublisher(publisher);
        HttpEntity<Item> itemHttpEntity = new HttpEntity<>(item);
        responseEntity = REST.postForEntity(URL, itemHttpEntity, Item.class);
    }

    @Then("http response code is {int}")
    public void httpResponseCodeIs(int code) {
        Assertions.assertEquals(code, responseEntity.getStatusCodeValue());
    }

    @And("response body should contain title {string}")
    public void responseBodyShouldContainTitle(String title) {
        Assertions.assertEquals(title, Objects.requireNonNull(responseEntity.getBody()).getTitle());
    }

    @And("response body should contain description {string}")
    public void responseBodyShouldContainDescription(String description) {
        Assertions.assertEquals(description, Objects.requireNonNull(responseEntity.getBody()).getDescription());
    }

    @And("response body should contain price {string}")
    public void responseBodyShouldContainPricePrice(int price) {
        Assertions.assertEquals(price, Objects.requireNonNull(responseEntity.getBody()).getPrice().intValue());
    }

    @And("response body should contain Artist name {string}")
    public void responseBodyShouldContainArtistName(String name) {
        Assertions.assertEquals(name, Objects.requireNonNull(responseEntity.getBody()).getArtist().getName());
    }

    @And("response body should contain Publisher name {string}")
    public void responseBodyShouldContainPublisherName(String name) {
        Assertions.assertEquals(name, Objects.requireNonNull(responseEntity.getBody()).getPublisher().getName());
    }

    @When("I get item with title {string}")
    public void iGetItemWithTitle(String title) {

    }

    @When("I update item title")
    public void iUpdateItemTitle() {
    }

    @And("item with title {string} should contain price <price>")
    public void itemWithTitleShouldContainPricePrice(String arg0) {
    }

    @When("I delete item with title {string}")
    public void iDeleteItemWithTitle(String arg0) {
    }

    @And("item with title {string} should not be found")
    public void itemWithTitleShouldNotBeFound(String arg0) {
    }

}
