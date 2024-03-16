package com.microservice.stockdatastreamer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AlphaVantageService {

    @Value("${api.key}")
    private String apikey;

    private final RestTemplate restTemplate;

    public AlphaVantageService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String fetchData() {
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=5min&apikey=" + apikey;
        String response = restTemplate.getForObject(url, String.class);
        System.out.println("Response: " + response);
        return response;
    }

}
