package com.shiryaeva.wyrgorod.cucumber;

import com.shiryaeva.wyrgorod.model.Artist;
import com.shiryaeva.wyrgorod.service.ArtistService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNull;

@TestPropertySource(locations = {"classpath:application.properties"})
public class CrudOnArtistSteps {

    @Autowired
    private ArtistService artistService;

    private ResponseEntity<Artist> responseEntity;


    @When("I create artist with name {string}")
    public void iCreateArtistWithName(String name) {
        responseEntity = tryToCreateArtist(name);
    }

    @Then("the response code should be {int}")
    public void theResponseCodeShouldBe(int code) {
        Assertions.assertEquals(code, responseEntity.getStatusCodeValue());
    }

    @When("I get artist with name {string}")
    public void iGetArtistWithName(String name) {
        responseEntity = new ResponseEntity<>(artistService.findByName(name), HttpStatus.OK);
    }

    @And("the response body should contain name {string}")
    public void theResponseBodyShouldContainName(String name) {
        Assertions.assertEquals(name, Objects.requireNonNull(responseEntity.getBody()).getName());
    }

    @When("I update artist's name")
    public void iUpdateArtistSName(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> columns : rows) {
            responseEntity = tryToUpdateArtist(columns.get("name"), columns.get("newName"));
        }
    }

    @When("I delete artist with name {string}")
    public void iDeleteArtistWithName(String name) {
        artistService.deleteByName(name);
        responseEntity = new ResponseEntity<>(HttpStatus.OK);
    }

    @And("artist with name {string} should not be found")
    public void artistWithNameShouldNotBeFound(String name) {
        assertNull(artistService.findByName(name));
    }

    private ResponseEntity<Artist> tryToUpdateArtist(String name, String newName) {
        responseEntity = new ResponseEntity<>(artistService.updateName(name, newName), HttpStatus.ACCEPTED);
        return responseEntity;
    }

    private ResponseEntity<Artist> tryToCreateArtist(String name) {
        if (artistService.findByName(name) == null) {
            Artist artist = new Artist(name);
            artistService.save(artist);
            responseEntity = new ResponseEntity<>(artist, HttpStatus.CREATED);
            return responseEntity;
        }
        return new ResponseEntity<>(null, HttpStatus.CONFLICT);
    }


}
