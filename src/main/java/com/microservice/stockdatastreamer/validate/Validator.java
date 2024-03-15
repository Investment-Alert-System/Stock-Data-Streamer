package com.microservice.stockdatastreamer.validate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.microservice.stockdatastreamer.exception.DataValidationException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class Validator {


    private final JsonSchema APIDataSchema;

    public Validator() throws DataValidationException {
        try
        {
            JsonSchemaFactory schemaFactory = JsonSchemaFactory.byDefault();
            this.APIDataSchema = schemaFactory.getJsonSchema("file:src/main/resources/APIDataSchema.json");
        } catch ( ProcessingException e) {
            throw new DataValidationException(e.getMessage());
        }
    }

    public ProcessingReport validateResponseMetaData(String jsonData) throws JsonProcessingException, ProcessingException {
        return this.APIDataSchema.validate(new ObjectMapper().readTree(jsonData));
    }

}
