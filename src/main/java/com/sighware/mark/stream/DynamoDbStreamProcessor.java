package com.sighware.mark.stream;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemUtils;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.PutRecordResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sighware.mark.server.event.RegistrationNumberEvent;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Processes new image events from a DynamoDB stream on the RegistrationNumberEvent table. Reads from the DynamoDB stream, converts
 * data to a json RegistrationNumberEvent and then writes the json to the kinesis stream
 */
public class DynamoDbStreamProcessor implements
        RequestHandler<DynamodbEvent, String> {

    private static final String KINESIS_STREAM_NAME = System.getenv("KINESIS_STREAM_NAME");
    private final AmazonKinesis kinesis = AmazonKinesisClientBuilder.defaultClient();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public String handleRequest(DynamodbEvent ddbEvent, Context context) {

        String sequence = null;
        for (DynamodbStreamRecord record : ddbEvent.getRecords()) {

            // Only process a new image
            Map<String, AttributeValue> newImage = record.getDynamodb().getNewImage();
            if (newImage != null) {

                // Load the stream data to be converted into json
                List<Map<String, AttributeValue>> listOfMaps = new ArrayList<>();
                listOfMaps.add(newImage);
                List<Item> itemList = ItemUtils.toItemList(listOfMaps);

                for (Item item : itemList) {
                    String json = item.toJSON();

                    System.out.println(json);

                    // Now write customer event to kinesis stream
                    RegistrationNumberEvent event = null;
                    try {
                        event = objectMapper.readValue(json, RegistrationNumberEvent.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    ByteBuffer data = ByteBuffer.wrap(json.getBytes(StandardCharsets.UTF_8));

                    // Ensure the records are written in sequence order
                    if (sequence == null) {
                        PutRecordResult r = kinesis.putRecord(KINESIS_STREAM_NAME, data,
                                event.getMark() + "-" + event.getCreateTime());
                        sequence = r.getSequenceNumber();
                    } else {
                        PutRecordResult r = kinesis.putRecord(KINESIS_STREAM_NAME, data,
                                event.getMark() + "-" + event.getCreateTime(), sequence);
                        sequence = r.getSequenceNumber();
                    }
                }
            }
        }
        return "Ok";
    }
}