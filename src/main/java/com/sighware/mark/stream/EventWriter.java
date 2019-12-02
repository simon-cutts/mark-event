package com.sighware.mark.stream;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sighware.mark.server.event.RegistrationNumberEvent;

import java.io.IOException;

/**
 * Writes JSON representations of RegistrationNumberEvent to an S3 bucket
 */
public class EventWriter {

    private final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Write RegistrationNumberEvent to the S3 bucket
     *
     * @param bucket
     * @param event
     * @return
     */
    public String write(String bucket, RegistrationNumberEvent event) {
        String key = event.getMark() + "-" + event.getCreateTime() + "-" + event.getEventName();
        try {
            s3.putObject(bucket, key, objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return "Ok";
    }

    /**
     * Writes JSON representations of RegistrationNumberEvent to an S3 bucket
     *
     * @param bucket
     * @param json
     * @return
     */
    public String write(String bucket, String json) {
        try {
            return write(bucket, objectMapper.readValue(json, RegistrationNumberEvent.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
