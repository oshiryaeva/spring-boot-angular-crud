package com.shiryaeva.wyrgorod.cucumber;

import com.shiryaeva.wyrgorod.model.Customer;
import com.shiryaeva.wyrgorod.service.CustomerService;
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
public class CrudOnCustomerSteps {

    @Autowired
    private CustomerService customerService;

    private ResponseEntity<Customer> responseEntity;


    @When("I create customer with first name {string} and last name {string} and email {string}")
    public void iCreateCustomerWithFirstNameAndLastNameAndEmail(String firstName, String lastName, String email) {
        responseEntity = tryToCreateCustomer(firstName, lastName, email);
    }

    @Then("http response code should be {int}")
    public void httpResponseCodeShouldBe(int code) {
        Assertions.assertEquals(code, responseEntity.getStatusCodeValue());
    }

    @When("I get customer with email {string}")
    public void iGetCustomerWithEmail(String email) {
        responseEntity = new ResponseEntity<>(customerService.findByEmail(email), HttpStatus.OK);
    }

    @And("http response body should contain first name {string}")
    public void httpResponseBodyShouldContainFirstName(String firstName) {
        Assertions.assertEquals(firstName, Objects.requireNonNull(responseEntity.getBody()).getFirstName());
    }

    @And("http response body should contain last name {string}")
    public void httpResponseBodyShouldContainLastName(String lastName) {
        Assertions.assertEquals(lastName, Objects.requireNonNull(responseEntity.getBody()).getLastName());
    }

    @And("http response body should contain email {string}")
    public void httpResponseBodyShouldContainEmail(String email) {
        Assertions.assertEquals(email, String.valueOf(Objects.requireNonNull(responseEntity.getBody()).getEmail()));
    }


    @When("I update customer's email")
    public void iUpdateCustomerSEmail(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> columns : rows) {
            responseEntity = tryToUpdateCustomer(columns.get("email"), columns.get("newEmail"));
        }
    }

    @And("customer with email {string} should contain first name {string}")
    public void customerWithIdShouldContainFirstName(String email, String firstName) {
        Assertions.assertEquals(firstName, customerService.findByEmail(email).getFirstName());
    }

    @And("customer with email {string} should contain last name {string}")
    public void customerWithIdShouldContainLastName(String email, String lastName) {
        Assertions.assertEquals(lastName, customerService.findByEmail(email).getLastName());
    }

    @When("I delete customer with email {string}")
    public void iDeleteCustomerWithId(String email) {
        customerService.deleteByEmail(email);
        responseEntity = new ResponseEntity<>(HttpStatus.OK);

    }

    @And("customer with email {string} should not be found")
    public void customerWithIdShouldNotBeFound(String email) {
        assertNull(customerService.findByEmail(email));
    }

    private ResponseEntity<Customer> tryToUpdateCustomer(String oldEmail, String newEmail) {
        responseEntity = new ResponseEntity<>(customerService.updateEmail(oldEmail, newEmail), HttpStatus.ACCEPTED);
        return responseEntity;
    }

    private ResponseEntity<Customer> tryToCreateCustomer(String firstName, String lastName, String email) {
        if (customerService.findByEmail(email) == null) {
            Customer customer = new Customer(firstName, lastName, email);
            customerService.save(customer);
            responseEntity = new ResponseEntity<>(customer, HttpStatus.CREATED);
            return responseEntity;
        }
        return new ResponseEntity<>(null, HttpStatus.CONFLICT);
    }


}
