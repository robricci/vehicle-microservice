package it.unisannio.vehicle.dto;

import it.unisannio.vehicle.dto.internal.Station;

import java.io.Serializable;

public class ManualDisplacementDTO implements Serializable {

    private String routeId;
    private Station station;

    public ManualDisplacementDTO() { }

    public ManualDisplacementDTO(String routeId, Station station) {
        this.routeId = routeId;
        this.station = station;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }
}
