package com.microservice.stockdatastreamer.controller;


import com.microservice.stockdatastreamer.core.ConfigurationHandler;
import com.microservice.stockdatastreamer.exception.LimitHandlingException;
import com.microservice.stockdatastreamer.service.StockSymbol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/datahandler/configuration")
public class ConfigurationController {

    private final ConfigurationHandler configurationHandler;

    @Autowired
    public ConfigurationController(ConfigurationHandler configurationHandler) {
        this.configurationHandler=configurationHandler;
    }


    @PostMapping("/setDataPoints")
    public ResponseEntity<String> setDataPoints(@RequestBody List<String> dataPoints) throws IOException {
        if (dataPoints.isEmpty()) {
            return sendResponse("Invalid length of data points", HttpStatus.BAD_REQUEST);
        }
        List<String> extendedDataPoints = ConfigurationHandler.getDataPointsFromFile();
        extendedDataPoints.addAll(dataPoints);
        int savedDataPoints = configurationHandler.handleDataPoints(extendedDataPoints);
        if (!(savedDataPoints > 0)) {
            return sendResponse("Invalid saved data points", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return sendResponse("Saved data points", HttpStatus.OK);
        }
    }

    @PostMapping("/setAlerts")
    public ResponseEntity<String> setAlertLimitPerSymbol(@RequestBody Map<String, Double> alertMap) throws IOException, LimitHandlingException {
        if (alertMap.isEmpty()) {
            return sendResponse("Invalid Data Points, please specify at least one symbol", HttpStatus.BAD_REQUEST);
        } else {
            Map<String, Double> extendedAlertMap = ConfigurationHandler.getAlertDataFromFile();
            extendedAlertMap.putAll(alertMap);
            int mapsize = configurationHandler.setAlertingForSymbols(extendedAlertMap);
            if (mapsize == 0) {
                return sendResponse("Invalid Data Points, please specify one valid symbol", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return sendResponse("Saved " + mapsize + " Alert Points", HttpStatus.OK);
        }
    }

    @GetMapping("/getAllStocks")
    public List<StockSymbol> getAllStocks() throws IOException {
        List<StockSymbol> symbolsList = ConfigurationHandler.getAllStocks();
        if (symbolsList.isEmpty()) {
            throw new IOException("Failed to read stock list");
        }
        return symbolsList;
    }

   // @GetMapping("/getAlerts")
    @DeleteMapping("/deleteSymbols")
    public ResponseEntity<String> deleteSymbols() {
        try {
            boolean success = configurationHandler.deleteDataPointsFromFile();
            if (success) {
                return sendResponse("Successfully deleted symbols", HttpStatus.OK);
            } else
                return sendResponse("Failed to delete symbols", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            e.getMessage();
            return sendResponse("Delete data points failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteAlerting")
    public ResponseEntity<String> deleteAlerting() {
        try {
            boolean success = configurationHandler.deleteAlertDataFromFile();
            if (success) {
               return sendResponse("Delete data points successful", HttpStatus.OK);
            } else
                return sendResponse("Delete data points failed", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            e.getMessage();
            return sendResponse("Delete data points failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private ResponseEntity<String> sendResponse(String response, HttpStatus status) {
        return ResponseEntity.status(status).body(response);
    }


}
