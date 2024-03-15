package com.microservice.stockdatastreamer;

import com.microservice.stockdatastreamer.core.DataHandler;
import com.microservice.stockdatastreamer.exception.DataValidationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StockdatastreamerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockdatastreamerApplication.class, args);
		DataHandler dataHandler = new DataHandler(new RestTemplateBuilder());
		try {
			dataHandler.fetchAndValidateData();
		} catch (DataValidationException e) {
			e.getCause();
		}

	}

}
