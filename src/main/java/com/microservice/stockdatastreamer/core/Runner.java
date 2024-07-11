package com.microservice.stockdatastreamer.core;


import com.microservice.stockdatastreamer.exception.DataValidationException;
import com.microservice.stockdatastreamer.service.DiscordMessenger;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class Runner {

    @Scheduled(fixedRate = 10000) // 308000 > 5 minutes
    public static void runDataStreamer() throws IOException, DataValidationException {
        DataHandler dataHandler = new DataHandler(new RestTemplateBuilder(), new DiscordMessenger());
        List<String> symbolsList = null;
        try {
            symbolsList = ConfigurationHandler.getDataPointsFromFile();
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
        if (symbolsList != null && !symbolsList.isEmpty()) {
            for (String symbol : symbolsList) {
                dataHandler.fetchAndValidateData(true, symbol);
            }
        }





    }
}
