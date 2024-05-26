package com.microservice.stockdatastreamer.controller;


import com.microservice.stockdatastreamer.core.ConfigurationHandler;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/datahandler/configuration")
public class ConfigurationController {

    private final ConfigurationHandler configurationHandler;

    @Autowired
    public ConfigurationController(ConfigurationHandler configurationHandler) {
        this.configurationHandler=configurationHandler;
    }


    @PostMapping("/setDataPoints")
    public ResponseEntity<String> setDataPoints(@RequestBody String[] dataPoints) {
        if (dataPoints.length == 0) {
            return sendResponse("Invalid length of data points", HttpStatus.BAD_REQUEST);
        }
        int savedDataPoints = configurationHandler.handleDataPoints(dataPoints);
        if (savedDataPoints == 0) {
            return sendResponse("Invalid saved data points", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return sendResponse("Saved data points", HttpStatus.OK);
        }
    }


    private ResponseEntity<String> sendResponse(String response, HttpStatus status) {
        return ResponseEntity.status(status).body(response);
    }


}
