package com.microservice.stockdatastreamer.core;

import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.microservice.stockdatastreamer.exception.DataValidationException;
import com.microservice.stockdatastreamer.exception.LimitHandlingException;
import com.microservice.stockdatastreamer.service.AlphaVantageService;
import com.microservice.stockdatastreamer.service.DiscordMessenger;
import com.microservice.stockdatastreamer.validate.Validator;
import org.joda.time.DateTime;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.util.List;
import java.util.Map;

public class DataHandler {

    private final RestTemplateBuilder restTemplateBuilder;
    private final DiscordMessenger discordMessenger;

    public DataHandler(RestTemplateBuilder restTemplateBuilder, DiscordMessenger discordMessenger) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.discordMessenger = discordMessenger; // Initialisiere das DiscordMessenger-Objekt
    }

    public void fetchAndValidateData(boolean realFetch, String symbol) throws DataValidationException {
        AlphaVantageService apiDataConsumer = new AlphaVantageService(restTemplateBuilder);
        String data;
        if (symbol == null || symbol.isEmpty()) {
            data = apiDataConsumer.fetchData(realFetch, symbol);
        } else if (DateTime.now().getDayOfWeek() <= 5) {
            data = apiDataConsumer.fetchData(realFetch, symbol);
        } else {
            data = "There are no supported symbols in this day! (Weekend??)";
        }

        try {
            Validator validator = new Validator();
            if (data.equals("There are no supported symbols in this day! (Weekend??)")) {
                System.out.println(data + "Date:" + DateTime.now());
            } else {
                ProcessingReport APIDataProcessingReport = validator.validateResponseMetaData(data);

                System.out.println("APIDataProcessingReport: " + APIDataProcessingReport);
                if (APIDataProcessingReport.isSuccess()) {

                    int dateindex = data.indexOf("3. Last Refreshed");
                    if (dateindex != -1) {
                        String date = data.substring(dateindex + 21, dateindex + 40);
                        date = date.replace(" ", "T");
                        DateTime dateTime = new DateTime(date);
//                        System.out.println(data);

                        if (dateTime.isBefore(DateTime.now()) && (DateTime.now().getMinuteOfHour() - dateTime.getMinuteOfHour()) < 5) {
                            LimitHandler limitHandler = new LimitHandler();
                            Map<String, Map<String, Double>> result;
                            try {
                                result = limitHandler.analyseLimits(data);
                            } catch (LimitHandlingException e) {
                                throw new LimitHandlingException(e.getMessage());
                            }
                            if (result != null) {
                                if (!result.isEmpty()) {
                                    for (Map.Entry<String, Map<String, Double>> dateEntry : result.entrySet()) {
                                        for (Map.Entry<String, Double> symbolEntry : dateEntry.getValue().entrySet()) {
                                            String alertDate = dateEntry.getKey().substring(0, dateEntry.getKey().indexOf(" "));
                                            String alertTime = dateEntry.getKey().substring(dateEntry.getKey().indexOf(" ") + 1);
                                            String stocksymbol = symbolEntry.getKey();
                                            Double value = symbolEntry.getValue();
                                            discordMessenger.sendToDiscord(
                                                    "https://discord.com/api/webhooks/1232976252726546492/N4vlTfxZiLh7YMiq2t3MeRMmN-HA6S5xvUAQpaIhQKDK-W8SLaDNkL_bLdKIJ4lJFQKy",
                                                    "Stock Alert! Date : " + alertDate +  "; Time: " + alertTime + "; Stock: " + stocksymbol + "; Value: " + value
                                            ); // Aufruf der Discord-Sendemethode
                                        }

                                    }
                                }
                            }

                        } else {
                            throw new DataValidationException("Refresh date is not valid or not in the last 5 minutes.");
                        }
                    } else {
                        throw new DataValidationException("Refresh date is not found.");
                    }

                } else {
                    System.err.println("Data Validation Failed: " + data);
                    throw new DataValidationException("Data Validation Failed");
                }
            }
        } catch (Exception e) {
            throw new DataValidationException(e.getMessage());
        }
    }
}
