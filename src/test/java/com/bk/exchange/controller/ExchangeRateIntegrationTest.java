package com.bk.exchange.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)

/**
 * Integration test for the exchange rate services 
 */
public class ExchangeRateIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void exchangeRate() {
        final ResponseEntity<?> response = restTemplate.getForEntity(createURLWithPort("/2019-08-30/USD/EUR"),
                Object.class);
        final ResponseEntity<?> responseFailure = restTemplate.getForEntity(createURLWithPort("/2019-08-jj/USD/EUR"),
                Object.class);
        final ResponseEntity<?> responseHistory = restTemplate.getForEntity(createURLWithPort("/history/daily/2019/08" +
                        "/30"),
                Object.class);
        final ResponseEntity<?> responseHistoryFailure = restTemplate.getForEntity(createURLWithPort("/history/daily" +
                        "/2019/yy/30"),
                Object.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(responseFailure.getStatusCode(), is(BAD_REQUEST));
        assertThat(responseHistory.getStatusCode(), is(OK));
        assertThat(responseHistoryFailure.getStatusCode(), is(BAD_REQUEST));
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + "/api/exchange-rate" + uri;
    }

}
