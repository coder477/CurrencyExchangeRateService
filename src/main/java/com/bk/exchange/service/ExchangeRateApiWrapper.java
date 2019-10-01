package com.bk.exchange.service;

import com.bk.exchange.exception.BadRequestException;
import com.bk.exchange.exception.ExternalApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ExchangeRateApiWrapper {

    private final String CURRENCY_HISTORY_API = "/history";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Getting exchange data from the source API
     */

    public JSONObject getExchangeBetween(String start, String end, String base, String symbol) {

        HttpHeaders requestHeaders = new HttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(requestHeaders);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(CURRENCY_HISTORY_API)
                .queryParam("start_at", start)
                .queryParam("end_at", end)
                .queryParam("base", base)
                .queryParam("symbols", symbol);
        try {
            ResponseEntity<?> responseEntity = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    requestEntity,
                    Object.class
            );
            return new JSONObject(objectMapper.writeValueAsString(responseEntity.getBody()));
        } catch (HttpStatusCodeException httpException) {
            if (httpException.getStatusCode().is4xxClientError()) {
                throw new BadRequestException(httpException.getStatusCode(), httpException.getResponseBodyAsString());
            } else {
                throw new ExternalApiException("Failed calling the data api", httpException.getResponseBodyAsString());
            }
        } catch (JsonProcessingException exception) {
            throw new ExternalApiException("Failed to parse JSON", exception.getMessage());
        }
    }
}
