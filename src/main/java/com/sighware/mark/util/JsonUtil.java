package com.sighware.mark.util;

public class JsonUtil {

    private JsonUtil() {
    }

    /**
     * Get the mark from the json, for example: {"mark": "WPPT2F5Q25","createTime:" ..."
     *
     * @param json
     * @return
     */
    public static String getMark(String json) {
        json = json.replace(" ", "").replace("\n", "");
        int s = json.indexOf("mark\":\"") + 7;
        String trim = json.substring(s);
        s = trim.indexOf('"');
        return trim.substring(0, s);
    }

    /**
     * Get the createTime from the json, for example: {"mark": "WPPT2F5Q25","createTime:" ..."
     *
     * @param json
     * @return
     */
    public static String getCreateTime(String json) {
        json = json.replace(" ", "").replace("\n", "");
        int s = json.indexOf("createTime\":\"") + 13;
        String trim = json.substring(s);
        s = trim.indexOf('"');
        return trim.substring(0, s);
    }

    /**
     * Get the eventName from the json, for example: {"eventName": "EntitlementCreatedEvent","createTime:" ..."
     *
     * @param json
     * @return
     */
    public static String getEventName(String json) {
        json = json.replace(" ", "").replace("\n", "");
        int s = json.indexOf("eventName\":\"") + 12;
        String trim = json.substring(s);
        s = trim.indexOf('"');
        return trim.substring(0, s);
    }
}
