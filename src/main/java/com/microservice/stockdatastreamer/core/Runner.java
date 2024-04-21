package com.microservice.stockdatastreamer.core;


import com.microservice.stockdatastreamer.exception.DataValidationException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class Runner {

    @Scheduled(fixedRate = 308000) // > 5 minutes
    public static void runDataStreamer() throws DataValidationException {
        DataHandler dataHandler = new DataHandler(new RestTemplateBuilder());
        dataHandler.fetchAndValidateData(true);
    }
}
