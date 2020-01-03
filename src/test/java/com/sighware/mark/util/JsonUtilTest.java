package com.sighware.mark.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonUtilTest {

    @Test
    public void testMark() {
        String eventSnippet = "{\n" +
                "  \"eventId\": \"90a9a11e-95b2-4c0d-aef3-53a15bfbda8f\",\n" +
                "  \"createTime\": \"2019-12-06T15:20:40.152Z\",\n" +
                "  \"eventName\": \"EntitlementCreatedEvent\"," +
                "  \"mark\": \"WPPT2F5Q25\"}";

        assertEquals("WPPT2F5Q25", JsonUtil.getMark(eventSnippet));
    }

    @Test
    public void testGetMarkNoSpace() {
        String eventSnippet = "{\"eventId\":\"90a9a11e-95b2-4c0d-aef3-53a15bfbda8f\"," +
                "  \"createTime\": \"2019-12-06T15:20:40.152Z\"," +
                "  \"eventName\": \"EntitlementCreatedEvent\"," +
                "  \"mark\": \"WPPT2F5Q25\"}";

        assertEquals("WPPT2F5Q25", JsonUtil.getMark(eventSnippet));
    }

    @Test
    public void testCreateTime() {
        String eventSnippet = "{\n" +
                "  \"eventId\": \"90a9a11e-95b2-4c0d-aef3-53a15bfbda8f\",\n" +
                "  \"createTime\": \"2019-12-06T15:20:40.152Z\",\n" +
                "  \"eventName\": \"EntitlementCreatedEvent\"," +
                "  \"mark\": \"WPPT2F5Q25\"}";

        assertEquals("2019-12-06T15:20:40.152Z", JsonUtil.getCreateTime(eventSnippet));
    }

    @Test
    public void tesEventName() {
        String eventSnippet = "{\n" +
                "  \"eventId\": \"90a9a11e-95b2-4c0d-aef3-53a15bfbda8f\",\n" +
                "  \"createTime\": \"2019-12-06T15:20:40.152Z\",\n" +
                "  \"eventName\": \"EntitlementCreatedEvent\"," +
                "  \"mark\": \"WPPT2F5Q25\"}";

        assertEquals("EntitlementCreatedEvent", JsonUtil.getEventName(eventSnippet));
    }

}