package com.shiryaeva.wyrgorod.cucumber;

import com.shiryaeva.wyrgorod.WyrgorodApplication;
import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = {WyrgorodApplication.class})
public class SpringGlue {

    private static final Logger LOG = LoggerFactory.getLogger(SpringGlue.class);

    @Before
    public void setUp() {
        LOG.info("Spring context initialized.");
        LOG.info("Starting Cucumber tests...");
    }

}
