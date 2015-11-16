package com.github.dddpaul.connectors;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")
public class ApplicationTest extends Assert {

    @Value("${local.server.port}")
    private int port;

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @Test
    public void contextLoads() {
    }

    /**
     * One of these tests will fail because Spring Test Framework initialize only one connector
     */

    @Test
    public void testControllerOne() throws Exception {
        RestTemplate rest = new TestRestTemplate();
        ResponseEntity<String> entity = rest.getForEntity(getBaseUrl() + "/one?name=Paul", String.class);
        assertThat(entity.getBody(), is("ControllerOne says \"Hello, Paul, I'm shared service\""));
        URI uri = new URI(getBaseUrl() + "/one?name=%D0%9F%D0%B0%D0%B2%D0%B5%D0%BB"); // "Павел" in utf-8
        entity = rest.getForEntity(uri, String.class);
        assertThat(entity.getBody(), is("ControllerOne says \"Hello, Павел, I'm shared service\""));
    }

    @Test
    public void testControllerTwo() throws Exception {
        RestTemplate rest = new TestRestTemplate();
        ResponseEntity<String> entity = rest.getForEntity(getBaseUrl() + "/two?name=Paul", String.class);
        assertThat(entity.getBody(), is("ControllerTwo says \"Hello, Paul, I'm shared service\""));
        URI uri = new URI(getBaseUrl() + "/two?name=%CF%E0%E2%E5%EB"); // "Павел" in cp1251
        entity = rest.getForEntity(uri, String.class);
        assertThat(entity.getBody(), is("ControllerTwo says \"Hello, Павел, I'm shared service\""));
    }
}
