package com.microservice.stockdatastreamer.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.stockdatastreamer.exception.LimitHandlingException;
import com.microservice.stockdatastreamer.service.StockSymbol;
import lombok.Getter;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Getter
@Service
@Repository
public class ConfigurationHandler {

   private static final String DATA_POINTS_FILE_PATH = "src/main/resources/dataPoints.json";
   private static final String ALERTING_FILE_PATH = "src/main/resources/alertData.json";
   private static final String ALL_STOCKS_FILE_PATH = "src/main/resources/stockSymbols.json";
   private static final ObjectMapper objectMapper = new ObjectMapper();

    public int handleDataPoints(List<String> dataPointsFromEndpoint) throws IOException {
        if (!dataPointsFromEndpoint.isEmpty()) {
            File dataPointsFile = new File(DATA_POINTS_FILE_PATH);
            for (String dataPoint : dataPointsFromEndpoint) {
                dataPoint = dataPoint.trim().toUpperCase();
                if (dataPoint.length() > 4) {
                    return -1;
                }
            }
            objectMapper.writeValue(dataPointsFile, dataPointsFromEndpoint);
            return dataPointsFromEndpoint.size();
        } else
            return -1;
    }

    public int setAlertingForSymbols(Map<String, Double> symbolLimitsMap) throws IOException, LimitHandlingException {
        File alertDataFile = new File(ALERTING_FILE_PATH);
        for (Map.Entry<String, Double> entry : symbolLimitsMap.entrySet()) {
            String symbol = entry.getKey();
            double limit = entry.getValue();
            if (!(symbol.length() <= 5) && !(limit > 0)){
                throw new LimitHandlingException("Symbol or limit are not permitted! Stock:" + symbol + " Limit:" + limit);
            }
        }
        objectMapper.writeValue(alertDataFile, symbolLimitsMap);
        return symbolLimitsMap.keySet().size();
    }

    public static List<String> getDataPointsFromFile() throws IOException {
        File dataPointsFile = new File(DATA_POINTS_FILE_PATH);
        if (!dataPointsFile.exists()) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(dataPointsFile, new TypeReference<>() {
        });
    }

    public static List<StockSymbol> getAllStocks() throws IOException {
        Map<String, String> allStockSymbols = getStocksFromFile();
        List<StockSymbol> stockSymbolsList = new ArrayList<>();
        if (!allStockSymbols.isEmpty()) {
            for (Map.Entry<String, String> entry : allStockSymbols.entrySet()) {
                StockSymbol stockSymbol = new StockSymbol(entry.getKey(), entry.getValue());
                stockSymbolsList.add(stockSymbol);
            }
        }
        return stockSymbolsList;
    }

    private static Map<String, String> getStocksFromFile() throws IOException {
        File stockSymbolsFile = new File(ALL_STOCKS_FILE_PATH);
        if (!stockSymbolsFile.exists()) {
            return new HashMap<>();
        }
        return objectMapper.readValue(stockSymbolsFile, new TypeReference<>() {});
    }

    public static Map<String, Double> getAlertDataFromFile() throws IOException {
        File alertDataFile = new File(ALERTING_FILE_PATH);
        if (!alertDataFile.exists()) {
            return new HashMap<>();
        }
        return objectMapper.readValue(alertDataFile, new TypeReference<>() {});
    }

    public boolean deleteDataPointsFromFile() throws IOException {
        File dataPointsFile = new File(DATA_POINTS_FILE_PATH);
        if (!dataPointsFile.exists()) {
            return false;
        } else
            return dataPointsFile.delete();
    }

    public boolean deleteAlertDataFromFile() throws IOException {
        File alertDataFile = new File(ALERTING_FILE_PATH);
        if (!alertDataFile.exists()) {
            return false;
        } else
            return alertDataFile.delete();
    }


}
