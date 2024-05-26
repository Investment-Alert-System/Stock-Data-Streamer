package com.microservice.stockdatastreamer.core;

import lombok.Getter;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Getter
@Service
@Repository
public class ConfigurationHandler {

    List<String> dataPointsList = new ArrayList<>();

    public int handleDataPoints(String[] dataPointsFromEndpoint) {
        for (String dataPoint : dataPointsFromEndpoint) {
            dataPointsList.add(dataPoint.trim().toUpperCase());
        }
        return dataPointsList.size();
    }

}
