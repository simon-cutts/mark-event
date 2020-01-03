package com.sighware.mark.stream;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.sighware.mark.util.JsonUtil;

/**
 * Writes JSON representations of RegistrationNumberEvent to an S3 bucket
 */
public class EventWriter {

    private final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();

    /**
     * Writes JSON representations of RegistrationNumberEvent to an S3 bucket
     *
     * @param bucket
     * @param json
     * @return
     */
    public void write(String bucket, String json) {
        String key = JsonUtil.getMark(json) + "-" + JsonUtil.getCreateTime(json) + "-" + JsonUtil.getEventName(json);
        s3.putObject(bucket, key, json);
    }
}
