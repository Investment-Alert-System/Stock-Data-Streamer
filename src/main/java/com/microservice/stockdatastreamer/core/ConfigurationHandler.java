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
import java.nio.file.Path;
import java.util.*;

@Getter
@Service
@Repository
public class ConfigurationHandler {

   private static final ObjectMapper objectMapper = new ObjectMapper();

    public int handleDataPoints(List<String> dataPointsFromEndpoint) throws IOException {
        if (!dataPointsFromEndpoint.isEmpty()) {
            File dataPointsFile = new File(getFilePath("data"));
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
        File alertDataFile = new File(getFilePath("alert"));
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
        File dataPointsFile = new File(getFilePath("data"));
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
        File stockSymbolsFile = new File(getFilePath("all"));
        if (!stockSymbolsFile.exists()) {
            return new HashMap<>();
        }
        return objectMapper.readValue(stockSymbolsFile, new TypeReference<>() {});
    }

    public static Map<String, Double> getAlertDataFromFile() throws IOException {
        File alertDataFile = new File(getFilePath("alert"));
        if (!alertDataFile.exists()) {
            return new HashMap<>();
        }
        return objectMapper.readValue(alertDataFile, new TypeReference<>() {});
    }

    public boolean deleteDataPointsFromFile() throws IOException {
        File dataPointsFile = new File(getFilePath("data"));
        if (!dataPointsFile.exists()) {
            return false;
        } else
            return dataPointsFile.delete();
    }

    public boolean deleteAlertDataFromFile() throws IOException {
        File alertDataFile = new File(getFilePath("alert"));
        if (!alertDataFile.exists()) {
            return false;
        } else
            return alertDataFile.delete();
    }

    private static String getFilePath (String fileContent) {
        boolean testpath = new File("src/main/resources/APIDataSchema.json").exists();
        String path = "";
        if (testpath) {
            path = "src/main/resources";
        } else
            path = "usr/local/lib/sds-build";

        switch (fileContent) {
            case "data":
                return path + "/dataPoints.json";
            case "alert":
                return path + "/alertData.json";
            case "all":
                return path + "/stockSymbols.json";
        }
        return "Wrong file content";
    }



}
