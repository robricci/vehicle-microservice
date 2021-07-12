package it.unisannio.vehicle.dto;

import it.unisannio.vehicle.dto.internal.Coordinate;
import it.unisannio.vehicle.dto.internal.Station;

import java.io.Serializable;
import java.util.List;

public class NextStationDTO implements Serializable {

    private Station nextStation;
    private List<Coordinate> minPath;

    public NextStationDTO() { }

    public NextStationDTO(Station nextStation) {
        this.nextStation = nextStation;
    }

    public NextStationDTO(Station nextStation, List<Coordinate> minPath) {
        this.nextStation = nextStation;
        this.minPath = minPath;
    }

    public Station getNextStation() {
        return nextStation;
    }

    public void setNextStation(Station nextStation) {
        this.nextStation = nextStation;
    }

    public List<Coordinate> getMinPath() {
        return minPath;
    }

    public void setMinPath(List<Coordinate> minPath) {
        this.minPath = minPath;
    }
}
