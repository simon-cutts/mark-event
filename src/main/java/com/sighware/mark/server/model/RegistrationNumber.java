package com.sighware.mark.server.model;

public interface RegistrationNumber {

    String getMark();

    void setMark(String mark);

    String getStatus();

    void setStatus(String status);

    String getEventTime();

    void setEventTime(String eventTime);

    Double getPrice();

    void setPrice(Double price);

    Long getVersion();

    void setVersion(Long version);

    Entitlement getEntitlement();

    void setEntitlement(Entitlement entitlement);

}
