package com.microservice.stockdatastreamer.core;


import com.microservice.stockdatastreamer.exception.DataValidationException;
import com.microservice.stockdatastreamer.service.DiscordMessenger;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class Runner {

    @Scheduled(fixedRate = 308000) // > 5 minutes
    public static void runDataStreamer() throws DataValidationException {
        DataHandler dataHandler = new DataHandler(new RestTemplateBuilder(), new DiscordMessenger());
        dataHandler.fetchAndValidateData(true);
    }
}
