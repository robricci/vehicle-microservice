package it.unisannio.vehicle.dto.external;

import java.io.Serializable;

public class Prediction implements Serializable {

    private Integer zoneId;
    private Integer numberOfRequests;

    public Integer getZoneId() {
        return zoneId;
    }

    public void setZoneId(Integer zoneId) {
        this.zoneId = zoneId;
    }

    public Integer getNumberOfRequests() {
        return numberOfRequests;
    }

    public void setNumberOfRequests(Integer numberOfRequests) {
        this.numberOfRequests = numberOfRequests;
    }
}
