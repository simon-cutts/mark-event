package com.sighware.mark.server.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Allows a RegistrationNumber to be persisted as a json document in the RegistrationNumberEvent table, wrapped
 * within an event
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrationNumberDocument implements RegistrationNumber {
    private String mark;
    private String status;
    private String eventTime;
    private Double price;
    private Long version;
    private Entitlement entitlement;

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Entitlement getEntitlement() {
        return entitlement;
    }

    public void setEntitlement(Entitlement entitlement) {
        this.entitlement = entitlement;
    }
}
