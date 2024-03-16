package com.microservice.stockdatastreamer.core;


import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class Runner {

    @Scheduled(fixedRate = 5000)
    public static void runDataStreamer() {
        DataHandler dataHandler = new DataHandler(new RestTemplateBuilder());
        dataHandler.fetchAndValidateData();
    }
}
