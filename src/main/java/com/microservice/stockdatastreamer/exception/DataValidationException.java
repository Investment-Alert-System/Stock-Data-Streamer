package com.microservice.stockdatastreamer.exception;

public class DataValidationException extends Exception{

    public DataValidationException(String message) {
        super(message);
    }
}
