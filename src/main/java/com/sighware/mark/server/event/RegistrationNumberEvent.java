package com.sighware.mark.server.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sighware.mark.server.model.RegistrationNumberDocument;

public class RegistrationNumberEvent {
    private RegistrationNumberDocument registrationNumber;
    private String createTime;
    private String eventId;
    private String eventName;
    private String mark;

    public RegistrationNumberEvent() {
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @JsonProperty("RegistrationNumber")
    public RegistrationNumberDocument getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(RegistrationNumberDocument registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
}
