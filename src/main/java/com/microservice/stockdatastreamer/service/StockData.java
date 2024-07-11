package com.microservice.stockdatastreamer.service;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.regex.Pattern;

@Getter
@Setter
public class StockData {

    @JsonProperty("Meta Data")
    private MetaData metaData;

    @JsonProperty("Time Series (5min)")
    private Map<String, DailyData> timeSeriesDaily;

    @Getter
    @Setter
    public static class MetaData {

        @JsonProperty("1. Information")
        private String information;
        @JsonProperty("2. Symbol")
        private String symbol;
        @JsonProperty("3. Last Refreshed")
        private String lastRefreshed;
        @JsonProperty("4. Interval")
        private String interval;
        @JsonProperty("5. Output Size")
        private String outputSize;
        @JsonProperty("6. Time Zone")
        private String timeZone;
    }

    @Getter
    @Setter
    public static class DailyData {

        @JsonProperty("1. open")
        private double open;
        @JsonProperty("2. high")
        private double high;
        @JsonProperty("3. low")
        private double low;
        @JsonProperty("4. close")
        private double close;
        @JsonProperty("5. volume")
        private double volume;
    }

}
