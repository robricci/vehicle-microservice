package it.unisannio.vehicle.dto.external;

import it.unisannio.vehicle.dto.internal.Station;

import java.io.Serializable;
import java.util.List;

public class ZonesRequest implements Serializable {

    private List<Station> points;

    public ZonesRequest() { }

    public ZonesRequest(List<Station> points) {
        this.points = points;
    }

    public List<Station> getPoints() {
        return points;
    }

    public void setPoints(List<Station> points) {
        this.points = points;
    }
}
