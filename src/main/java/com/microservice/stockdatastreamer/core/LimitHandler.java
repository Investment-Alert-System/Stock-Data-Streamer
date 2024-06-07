package com.microservice.stockdatastreamer.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.stockdatastreamer.exception.LimitHandlingException;
import com.microservice.stockdatastreamer.service.StockData;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LimitHandler {

    public Map<String, Map<String, Double>> analyseLimits (String jsonString) throws LimitHandlingException {
        StockData stockData = parseJsonToStockData(jsonString);
        Map<String, StockData.DailyData> dailyDataMap = stockData.getTimeSeriesDaily();
        String dataSymbol = stockData.getMetaData().getSymbol();
        Map<String, Double> alertMap;
        try {
            alertMap = ConfigurationHandler.getAlertDataFromFile();
        } catch (IOException e) {
            throw new LimitHandlingException("Failed to read Data from Alert File: " + e.getMessage());
        }
        if (alertMap.containsKey(dataSymbol)) {
            double alertHigh = alertMap.get(dataSymbol);
            Map<String, Map<String, Double>> resultMap = new HashMap<>();
            for (Map.Entry<String, StockData.DailyData> entry : dailyDataMap.entrySet()) {
                StockData.DailyData dailyData = entry.getValue();
                double high = dailyData.getHigh();
                double low = dailyData.getLow();
                if (high >= alertHigh) {
                    resultMap.put(entry.getKey(), Map.of(dataSymbol, high));
                    return resultMap;
                }
            }
        } else
            System.out.println("No alerting data found for symbol " + dataSymbol);
        return null;
    }

    public StockData parseJsonToStockData(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, StockData.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
