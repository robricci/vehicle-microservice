package it.unisannio.vehicle.dto.internal;

import java.io.Serializable;
import java.util.List;

public class RouteDTO implements Serializable {

    private String id;
    private List<Station> stations;

    public RouteDTO() { }

    public RouteDTO(String id, List<Station> stations) {
        this.id = id;
        this.stations = stations;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }
}
