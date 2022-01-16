package com.shiryaeva.wyrgorod;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@AutoConfigureTestEntityManager
@CucumberOptions(
        features = {"src/test/resources/cucumber"},
        tags = "not @ignore",
        glue = {"com.shiryaeva.wyrgorod.cucumber"},
        plugin = {"pretty", "html:target/cucumber"},
        monochrome = true
)
@RunWith(Cucumber.class)
@SpringBootTest
@Testcontainers
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional
public class CucumberTest {

    @Container
    public static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("test")
            .withUsername("postgres")
            .withPassword("password");

    @BeforeAll
    static void startup() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void cleanup() {
        postgreSQLContainer.stop();
    }
}
