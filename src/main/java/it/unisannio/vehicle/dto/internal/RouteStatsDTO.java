package it.unisannio.vehicle.dto.internal;

import java.io.Serializable;
import java.util.List;

public class RouteStatsDTO implements Serializable {
    private String id;
    private int requests;
    private List<StationStatsDTO> stations;

    public RouteStatsDTO() { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRequests() {
        return requests;
    }

    public void setRequests(int requests) {
        this.requests = requests;
    }

    public List<StationStatsDTO> getStations() {
        return stations;
    }

    public void setStations(List<StationStatsDTO> stations) {
        this.stations = stations;
    }
}
