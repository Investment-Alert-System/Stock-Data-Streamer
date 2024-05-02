package com.microservice.stockdatastreamer.core;

import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.microservice.stockdatastreamer.exception.DataValidationException;
import com.microservice.stockdatastreamer.service.AlphaVantageService;
import com.microservice.stockdatastreamer.validate.Validator;
import org.joda.time.DateTime;
import org.springframework.boot.web.client.RestTemplateBuilder;
import com.microservice.stockdatastreamer.service.DiscordMessenger;

public class DataHandler {

    private final RestTemplateBuilder restTemplateBuilder;
    private final DiscordMessenger discordMessenger; // Hinzugefügtes Feld

    public DataHandler(RestTemplateBuilder restTemplateBuilder, DiscordMessenger discordMessenger) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.discordMessenger = discordMessenger; // Initialisiere das DiscordMessenger-Objekt
    }

    public void fetchAndValidateData(boolean realFetch) throws DataValidationException {
        AlphaVantageService apiDataConsumer = new AlphaVantageService(restTemplateBuilder);
        String data;
        if (DateTime.now().getDayOfWeek() <= 5) {
            data = apiDataConsumer.fetchData(realFetch);
        } else {
            data = "No data available on weekends.";
        }

        try {
            Validator validator = new Validator();
            if (data.equals("No data available on weekends.")) {
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

                        if (dateTime.isBefore(DateTime.now()) && (DateTime.now().getMinuteOfHour() - dateTime.getMinuteOfHour()) < 5) {
                            String message = "Data is freshly updated and validated!";
                            discordMessenger.sendToDiscord(
                                    "https://discord.com/api/webhooks/1232976252726546492/N4vlTfxZiLh7YMiq2t3MeRMmN-HA6S5xvUAQpaIhQKDK-W8SLaDNkL_bLdKIJ4lJFQKy",
                                    "Test from Kotlin"
                            ); // Aufruf der Discord-Sendemethode
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
